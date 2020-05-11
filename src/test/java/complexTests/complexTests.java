package complexTests;

import Implementations.*;
import Interfaces.*;
import common.DependencyException;
import complex.Injector;
import complex.Container;
import complexFactories.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;

public class complexTests {

    private Injector injector;

    @Before
    public void createContainer(){
        injector = new Container();
    }

    @Test
    public void registerDuplicatedObjects1() throws DependencyException{
        injector.registerConstant(Integer.class, 42);
        assertThrows(DependencyException.class, () -> {
            injector.registerConstant(Integer.class, 50);
        });
    }

    @Test
    public void registerDuplicatedObjects2() throws DependencyException{
        injector.registerFactory(InterfaceC.class, new FactoryC1());
        assertThrows(DependencyException.class, () -> {
            injector.registerFactory(InterfaceC.class, new FactoryC1());
        });
    }

    @Test
    public void registerDuplicatedObjects3() throws DependencyException{
        injector.registerSingleton(InterfaceD.class, new FactoryD1());
        assertThrows(DependencyException.class, () -> {
            injector.registerSingleton(InterfaceD.class, new FactoryD1());
        });
    }

    @Test
    public void getConstantObject() throws DependencyException{
        injector.registerConstant(Integer.class, 42);
        int d = injector.getObject(Integer.class);
        assertThat(d, is(42));
    }

    @Test
    public void getFactoryObject() throws DependencyException{
        injector.registerConstant(Integer.class, 42);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceD d = injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.getI(), is(42));
    }

    @Test
    public void getFactoryDisctincObject() throws DependencyException{
        injector.registerConstant(Integer.class, 42);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceD dInterface1 = injector.getObject(InterfaceD.class);
        assertThat(dInterface1, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) dInterface1;
        assertThat(d1.getI(), is(42));

        InterfaceD dInterface2 = injector.getObject(InterfaceD.class);
        assertThat(dInterface2, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d2 = (ImplementationD1) dInterface2;
        assertThat(d2.getI(), is(42));
        assertNotSame(d1, d2);
    }

    @Test
    public void getSingletonObject() throws DependencyException{
        injector.registerConstant(Integer.class, 42);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceD dInterface1 = injector.getObject(InterfaceD.class);
        assertThat(dInterface1, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) dInterface1;
        assertThat(d1.getI(), is(42));

        InterfaceD dInterface2 = injector.getObject(InterfaceD.class);
        assertThat(dInterface2, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d2 = (ImplementationD1) dInterface2;
        assertThat(d2.getI(), is(42));
        assertSame(d1, d2);
    }

    @Test
    public void getObjectNonRegistered() throws DependencyException{
        assertThrows(DependencyException.class, () -> {
            injector.getObject(InterfaceA.class);
        });
    }

    @Test
    public void getFactoryObjectNoDependencies() throws DependencyException{
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        assertThrows(DependencyException.class, () -> {
            injector.getObject(InterfaceD.class);
        });
    }

    @Test
    public void getSingletonObjectNoDependencies() throws DependencyException{
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        assertThrows(DependencyException.class, () -> {
            injector.getObject(InterfaceD.class);
        });
    }

    @Test
    public void testGetFactoryA1Instance() throws DependencyException{
        injector.registerConstant(ImplementationD1.class, new ImplementationD1(42));
        injector.registerConstant(String.class, "string1");

        injector.registerFactory(ImplementationB1.class, new FactoryB1(), ImplementationD1.class);
        injector.registerConstant(InterfaceB.class, injector.getObject(ImplementationB1.class));
        injector.registerFactory(ImplementationC1.class, new FactoryC1(), String.class);
        injector.registerConstant(InterfaceC.class, injector.getObject(ImplementationC1.class));

        injector.registerFactory(ImplementationA1.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        InterfaceA a = injector.getObject(ImplementationA1.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void testGetSingletonA1Instance() throws DependencyException{
        injector.registerConstant(ImplementationD1.class, new ImplementationD1(42));
        injector.registerConstant(String.class, "string1");

        injector.registerSingleton(ImplementationB1.class, new FactoryB1(), ImplementationD1.class);
        injector.registerConstant(InterfaceB.class, injector.getObject(ImplementationB1.class));
        injector.registerSingleton(ImplementationC1.class, new FactoryC1(), String.class);
        injector.registerConstant(InterfaceC.class, injector.getObject(ImplementationC1.class));

        injector.registerSingleton(ImplementationA1.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        InterfaceA a = injector.getObject(ImplementationA1.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }
}
