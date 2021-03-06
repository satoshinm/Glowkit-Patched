package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

// Paper start
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.destroystokyo.paper.event.executor.MethodHandleEventExecutor;
import com.destroystokyo.paper.event.executor.StaticMethodHandleEventExecutor;
import com.destroystokyo.paper.event.executor.asm.ASMEventExecutorGenerator;
import com.destroystokyo.paper.event.executor.asm.ClassDefiner;
import com.google.common.base.Preconditions;
// Paper end

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    public void execute(Listener listener, Event event) throws EventException;

    // Paper start
    public static EventExecutor create(Method m, Class<? extends Event> eventClass) {
        Preconditions.checkNotNull(m, "Null method");
        Preconditions.checkArgument(m.getParameterCount() != 0, "Incorrect number of arguments %s", m.getParameterCount());
        Preconditions.checkArgument(m.getParameterTypes()[0] == eventClass, "First parameter %s doesn't match event class %s", m.getParameterTypes()[0], eventClass);
        ClassDefiner definer = ClassDefiner.getInstance();
        if (Modifier.isStatic(m.getModifiers())) {
            return new StaticMethodHandleEventExecutor(eventClass, m);
        } if (definer.isBypassAccessChecks() || Modifier.isPublic(m.getDeclaringClass().getModifiers()) && Modifier.isPublic(m.getModifiers())) {
            String name = ASMEventExecutorGenerator.generateName();
            byte[] classData = ASMEventExecutorGenerator.generateEventExecutor(m, name);
            Class<? extends EventExecutor> c = definer.defineClass(m.getDeclaringClass().getClassLoader(), name, classData).asSubclass(EventExecutor.class);
            try {
                EventExecutor asmExecutor = c.newInstance();
                // Define a wrapper to conform to bukkit stupidity (passing in events that don't match and wrapper exception)
                return new EventExecutor() {
                    @Override
                    public void execute(Listener listener, Event event) throws EventException {
                        if (!eventClass.isInstance(event)) return;
                        try {
                            asmExecutor.execute(listener, event);
                        } catch (Exception e) {
                            throw new EventException(e);
                        }
                    }
                };
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AssertionError("Unable to initialize generated event executor", e);
            }
        } else {
            return new MethodHandleEventExecutor(eventClass, m);
        }
    }
    // Paper end
}
