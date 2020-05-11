package simpleTests;

import Implementations.*;
import Interfaces.*;
import common.DependencyException;
import simple.Container;
import simple.Injector;
import simpleFactories.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;



public class simpleTests {

    private Injector injector;

    @Before
    public void createContainer(){
        injector = new Container();
    }

    @Test
    public void registerDuplicatedObjects1() throws DependencyException{
        injector.registerConstant("I", 42);
        assertThrows(DependencyException.class, () -> {
           injector.registerFactory("I", new FactoryA1());
        });
    }

    @Test
    public void registerDuplicatedObjects2() throws DependencyException{
        injector.registerConstant("I", 42);
        assertThrows(DependencyException.class, () -> {
            injector.registerSingleton("I", new FactoryB1());
        });
    }

    @Test
    public void registerDuplicatedObjects3() throws DependencyException{
        injector.registerFactory("I", new FactoryC1());
        assertThrows(DependencyException.class, () -> {
            injector.registerConstant("I", 42);
        });
    }

    @Test
    public void registerDuplicatedObjects4() throws DependencyException{
        injector.registerFactory("I", new FactoryC1());
        assertThrows(DependencyException.class, () -> {
            injector.registerSingleton("I", new FactoryD1());
        });
    }

    @Test
    public void registerDuplicatedObjects5() throws DependencyException{
        injector.registerSingleton("I", new FactoryC1());
        assertThrows(DependencyException.class, () -> {
            injector.registerConstant("I", 42);
        });
    }

    @Test
    public void registerDuplicatedObjects6() throws DependencyException{
        injector.registerSingleton("I", new FactoryC1());
        assertThrows(DependencyException.class, () -> {
            injector.registerFactory("I", new FactoryD1());
        });
    }

    @Test
    public void getConstantObject() throws DependencyException{
        injector.registerConstant("I", 42);
        int d = (int) injector.getObject("I");
        assertThat(d, is(42));
    }

    @Test
    public void getFactoryObject() throws DependencyException{
        injector.registerConstant("I", 42);
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceD d = (InterfaceD) injector.getObject("D");
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.getI(), is(42));
    }

    @Test
    public void getFactoryDisctincObject() throws DependencyException{
        injector.registerConstant("I", 42);
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceD dInterface1 = (InterfaceD) injector.getObject("D");
        assertThat(dInterface1, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) dInterface1;
        assertThat(d1.getI(), is(42));

        InterfaceD dInterface2 = (InterfaceD) injector.getObject("D");
        assertThat(dInterface2, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d2 = (ImplementationD1) dInterface2;
        assertThat(d2.getI(), is(42));
        assertNotSame(d1, d2);
    }

    @Test
    public void getSingletonObject() throws DependencyException{
        injector.registerConstant("I", 42);
        injector.registerSingleton("D", new FactoryD1(), "I");
        InterfaceD dInterface1 = (InterfaceD) injector.getObject("D");
        assertThat(dInterface1, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) dInterface1;
        assertThat(d1.getI(), is(42));

        InterfaceD dInterface2 = (InterfaceD) injector.getObject("D");
        assertThat(dInterface2, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d2 = (ImplementationD1) dInterface2;
        assertThat(d2.getI(), is(42));
        assertSame(d1, d2);
    }

    @Test
    public void getObjectNonRegistered() throws DependencyException{
        assertThrows(DependencyException.class, () -> {
           injector.getObject("A");
        });
    }

    @Test
    public void getFactoryObjectNoDependencies() throws DependencyException{
        injector.registerFactory("D", new FactoryD1(), "I");
        assertThrows(DependencyException.class, () -> {
            injector.getObject("D");
        });
    }

    @Test
    public void getSingletonObjectNoDependencies() throws DependencyException{
        injector.registerFactory("D", new FactoryD1(), "I");
        assertThrows(DependencyException.class, () -> {
            injector.getObject("D");
        });
    }

    @Test
    public void getFactoryObjectWrongDependency() throws DependencyException{
        injector.registerConstant("I", "string1");
        injector.registerFactory("D", new FactoryD1(), "I");
        assertThrows(DependencyException.class, () -> {
            InterfaceD d = (InterfaceD) injector.getObject("D");
        });
    }

    @Test
    public void getSingletonObjectWrongDependency() throws DependencyException{
        injector.registerConstant("I", "string1");
        injector.registerSingleton("D", new FactoryD1(), "I");
        assertThrows(DependencyException.class, () -> {
            InterfaceD d = (InterfaceD) injector.getObject("D");
        });
    }

    @Test
    public void testGetFactoryA1Instance() throws DependencyException{
        injector.registerConstant("H", new ImplementationD1(42));
        injector.registerConstant("G", "string1");

        injector.registerFactory("B", new FactoryB1(), "H");
        injector.registerConstant("I", injector.getObject("B"));
        injector.registerFactory("C", new FactoryC1(), "G");
        injector.registerConstant("S", injector.getObject("C"));

        injector.registerFactory("A", new FactoryA1(), "I", "S");
        InterfaceA a =(InterfaceA)injector.getObject("A");
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void testGetSingletonA1Instance() throws DependencyException{
        injector.registerConstant("H", new ImplementationD1(42));
        injector.registerConstant("G", "string1");

        injector.registerSingleton("B", new FactoryB1(), "H");
        injector.registerConstant("I", injector.getObject("B"));
        injector.registerSingleton("C", new FactoryC1(), "G");
        injector.registerConstant("S", injector.getObject("C"));

        injector.registerSingleton("A", new FactoryA1(), "I", "S");
        InterfaceA a =(InterfaceA)injector.getObject("A");
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }
}
