package simpleTests;

import Implementations.ImplementationA1;
import Implementations.ImplementationB1;
import Implementations.ImplementationC1;
import Implementations.ImplementationD1;
import Interfaces.InterfaceA;
import Interfaces.InterfaceB;
import Interfaces.InterfaceC;
import Interfaces.InterfaceD;
import common.DependencyException;
import org.junit.Before;
import org.junit.Test;
import simple.Container;
import simple.Injector;
import simpleFactories.FactoryA1;
import simpleFactories.FactoryB1;
import simpleFactories.FactoryC1;
import simpleFactories.FactoryD1;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;


public class simpleTests {

    private Injector injector;

    @Before
    public void createContainer(){
        injector = new Container();
    }

    @Test
    public void exampleTestGetFactoryD1Instance() throws DependencyException{
        System.out.println("************ exampleTest ************");
        System.out.println("Tests if factoryD1 is correctly registered and gets an instance of it. \n");
        injector.registerConstant("I", 42);
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceD d = (InterfaceD) injector.getObject("D");
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
        System.out.println("\n ************ End of exampleTest ************ \n");
        }


    @Test
    public void test1GetFactoryA1Instance() throws DependencyException{
        System.out.println("************ Test 1 ************");
        System.out.println("Tests if factoryA1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        registerFactories();
        InterfaceA a =(InterfaceA)injector.getObject("A");
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
        System.out.println("\n************ End of Test 1 ************ \n");
    }




    @Test
    public void test2GetFactoryB1Instance() throws DependencyException{
        System.out.println("************ Test 2 ************");
        System.out.println("Tests if factoryB1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant("I", 42);
        injector.registerFactory("B", new FactoryB1(), "D");
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceB b = (InterfaceB) injector.getObject("B");
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
        System.out.println("\n************ End of Test 2 ************\n");

    }

    @Test
    public void test3GetFactoryC1Instance() throws DependencyException{
        System.out.println("************ Test 3 ************");
        System.out.println("Tests if factoryC1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant("S", "exampleString");
        injector.registerFactory("C", new FactoryC1(), "S");
        InterfaceC c = (InterfaceC) injector.getObject("C");
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("exampleString"));
        System.out.println("\n************ End of Test 3 ************\n");
     }


    @Test (expected = DependencyException.class)
    public void test4CreateFactoryA1WithoutConstant() throws DependencyException{
        System.out.println("************ Test 4 ************");
        System.out.println("Tests creation of FactoryA1 instance without a constant\n");
        injector.registerConstant("S", "exampleString");
        registerFactories();
        InterfaceA a = (InterfaceA) injector.getObject("A");
        System.out.println("\n************ End of Test 4 ************\n");
     }

    @Test (expected = DependencyException.class)
    public void test5CreateFactoryA1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 5 ************");
        System.out.println("Tests creation of FactoryA1 instance using wrong arguments\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        injector.registerFactory("A",  new FactoryA1(), "I", "S");
        InterfaceA a = (InterfaceA) injector.getObject("A");
        System.out.println("\n************ End of Test 5 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test6CreateFactoryB1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 6 ************");
        System.out.println("Tests creation of FactoryB1 instance using wrong arguments\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        injector.registerFactory("B", new FactoryB1(), "S");
        InterfaceB b = (InterfaceB) injector.getObject("B");
        System.out.println("\n************ End of Test 6 ************\n");
       }

    @Test (expected = DependencyException.class)
    public void test7CreateFactoryC1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 7 ************");
        System.out.println("Tests creation of FactoryC1 instance using wrong arguments\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        injector.registerFactory("C", new FactoryC1(), "I");
        InterfaceC c = (InterfaceC) injector.getObject("C");
        System.out.println("\n************ End of Test 7 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test8CreateFactoryD1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 8 ************");
        System.out.println("Tests if FactoryD1 is correctly registered using wrong arguments and trying to get an instance of it \n");
        String[] strings = {"string1", "string2", "string3"};
        injector.registerConstant("I", strings);
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceD d =(InterfaceD) injector.getObject("D");
        System.out.println("\n************ End of Test 8 ************\n");
    }


    @Test
    public void test9GetFactoryA1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 9 ************");
        System.out.println("Tests if factoryA1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        registerSingletons();
        InterfaceA a =(InterfaceA) injector.getObject("A");
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
        System.out.println("\n************ End of Test 9 ************\n");

    }

    @Test
    public void test10GetFactoryB1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 10 ************");
        System.out.println("Tests if factoryB1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant("I", 42);
        injector.registerSingleton("B", new FactoryB1(), "D");
        injector.registerSingleton("D", new FactoryD1(), "I");
        InterfaceB b = (InterfaceB) injector.getObject("B");
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
        System.out.println("\n************ End of Test 10 ************\n");
    }

    @Test
    public void test11GetFactoryC1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 11 ************");
        System.out.println("Tests if factoryC1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant("S", "exampleString");
        injector.registerSingleton("C", new FactoryC1(), "S");
        InterfaceC c = (InterfaceC) injector.getObject("C");
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("exampleString"));
        System.out.println("\n************ End of Test 11 ************\n");
    }

    @Test
    public void test12GetFactoryC1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 12 ************");
        System.out.println("Tests if factoryD1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant("I", 42);
        injector.registerSingleton("D", new FactoryD1(), "I");
        InterfaceD d = (InterfaceD) injector.getObject("D");
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
        System.out.println("\n************ End of Test 12 ************\n");
    }

    @Test(expected = DependencyException.class)
    public void test13RegisterSameConstantTwice() throws DependencyException{
        System.out.println("************ Test 13 ************");
        System.out.println("Tests if register the same constant twice.\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("I", 42);
        System.out.println("\n************ End of Test 13 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test14RegisterSameFactoryTwice() throws DependencyException{
        System.out.println("************ Test 14 ************");
        System.out.println("Tests if register the same factory twice.\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        injector.registerFactory("B", new FactoryB1(), "D");
        injector.registerFactory("B", new FactoryB1(), "D");
        System.out.println("\n************ End of Test 14 ************\n");
    }

    @Test
    public void test15checkgetObjectMethod() throws DependencyException{
        System.out.println("************ Test 15 ************");
        System.out.println("Checks if getObject() method returns different objects.\n");
        injector.registerConstant("I", 42);
        injector.registerConstant("S", "exampleString");
        registerFactories();
        ImplementationA1 A1 = (ImplementationA1) injector.getObject("A");
        ImplementationA1 A2 = (ImplementationA1) injector.getObject("A");
        ImplementationB1 B1 = (ImplementationB1) injector.getObject("B");
        ImplementationB1 B2 = (ImplementationB1) injector.getObject("B");
        ImplementationC1 C1 = (ImplementationC1) injector.getObject("C");
        ImplementationC1 C2 = (ImplementationC1) injector.getObject("C");
        ImplementationD1 D1 = (ImplementationD1) injector.getObject("D");
        ImplementationD1 D2 = (ImplementationD1) injector.getObject("D");
        assertFalse(A1==A2);
        assertFalse(B1==B2);
        assertFalse(C1==C2);
        assertFalse(D1==D2);
        System.out.println("\n************ End of Test 15 ************\n");
    }





    private void registerFactories() throws DependencyException{
        injector.registerFactory("A", new FactoryA1(), "B", "C");
        injector.registerFactory("B", new FactoryB1(), "D");
        injector.registerFactory("C", new FactoryC1(), "S");
        injector.registerFactory("D", new FactoryD1(), "I");

    }

    private void registerSingletons() throws DependencyException{
        injector.registerSingleton("A", new FactoryA1(), "B", "C");
        injector.registerSingleton("B", new FactoryB1(), "D");
        injector.registerSingleton("C", new FactoryC1(), "S");
        injector.registerSingleton("D", new FactoryD1(), "I");

    }







}
