package complexTests;

import Implementations.ImplementationA1;
import Implementations.ImplementationB1;
import Implementations.ImplementationC1;
import Implementations.ImplementationD1;
import Interfaces.InterfaceA;
import Interfaces.InterfaceB;
import Interfaces.InterfaceC;
import Interfaces.InterfaceD;
import common.DependencyException;
import complex.Injector;
import complexFactories.FactoryA1;
import complexFactories.FactoryB1;
import complexFactories.FactoryC1;
import complexFactories.FactoryD1;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class complexTests {

    private Injector injector;

    @Before
    public void createContainer(){
        injector = new complex.Container();
    }

    @Test
    public void exampleTestGetFactoryD1Instance() throws DependencyException {
        System.out.println("************ exampleTest ************");
        System.out.println("Tests if factoryD1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceD d = (InterfaceD) injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
        System.out.println("\n************ End of exampleTest ************\n");
    }

    @Test
    public void test1GetFactoryA1Instance() throws DependencyException{
        System.out.println("************ Test 1 ************");
        System.out.println("Tests if factoryA1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        registerFactories();
        InterfaceA a =(InterfaceA)injector.getObject(InterfaceA.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
        System.out.println("\n************ End of Test 1 ************\n");
    }

    @Test
    public void test2GetFactoryB1Instance() throws DependencyException{
        System.out.println("************ Test 2 ************");
        System.out.println("Tests if factoryB1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceB b = (InterfaceB) injector.getObject(InterfaceB.class);
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
        System.out.println("\n************ End of Test 2 ************\n");
      }

    @Test
    public void test3GetFactoryC1Instance() throws DependencyException{
        System.out.println("************ Test 3 ************");
        System.out.println("Tests if factoryC1 is correctly registered and gets an instance of it.\n");
        injector.registerConstant(String.class, "exampleString");
        injector.registerFactory(InterfaceC.class, new FactoryC1(), String.class);
        InterfaceC c = (InterfaceC) injector.getObject(InterfaceC.class);
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("exampleString"));
        System.out.println("\n************ End of Test 3 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test4CreateFactoryA1WithoutConstant() throws DependencyException{
        System.out.println("************ Test 4 ************");
        System.out.println("Tests creation of FactoryA1 instance without a constant\n");
        injector.registerConstant(String.class, "exampleString");
        registerFactories();
        InterfaceA a = (InterfaceA) injector.getObject(InterfaceA.class);
        System.out.println("\n************ End of Test 4 ************\n");

    }

    @Test (expected = DependencyException.class)
    public void test5CreateFactoryA1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 5 ************");
        System.out.println("Tests creation of FactoryA1 instance using wrong arguments\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        injector.registerFactory(InterfaceA.class,  new FactoryA1(), Integer.class, String.class);
        InterfaceA a = (InterfaceA) injector.getObject(InterfaceA.class);
        System.out.println("\n************ End of Test 5 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test6CreateFactoryB1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 6 ************");
        System.out.println("Tests creation of FactoryB1 instance using wrong arguments\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        injector.registerFactory(InterfaceB.class, new FactoryB1(), String.class);
        InterfaceB b = (InterfaceB) injector.getObject(InterfaceB.class);
        System.out.println("\n************ End of Test 6 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test7CreateFactoryC1UsingWrongArguments() throws DependencyException{
        System.out.println("************ Test 7 ************");
        System.out.println("Tests creation of FactoryC1 instance using wrong arguments\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        injector.registerFactory(InterfaceC.class, new FactoryC1(), Integer.class);
        InterfaceC c = (InterfaceC) injector.getObject(InterfaceC.class);
        System.out.println("\n************ End of Test 7 ************\n");
    }

    @Test
    public void test8GetFactoryA1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 8 ************");
        System.out.println("Tests if factoryA1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        registerSingletons();
        InterfaceA a =(InterfaceA) injector.getObject(InterfaceA.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
        System.out.println("\n************ End of Test 8 ************\n");
    }

    @Test
    public void test9GetFactoryB1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 9 ************");
        System.out.println("Tests if factoryB1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerSingleton(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceB b = (InterfaceB) injector.getObject(InterfaceB.class);
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
        System.out.println("\n************ End of Test 9 ************\n");
    }

    @Test
    public void test10GetFactoryC1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 10 ************");
        System.out.println("Tests if factoryC1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant(String.class, "exampleString");
        injector.registerSingleton(InterfaceC.class, new FactoryC1(), String.class);
        InterfaceC c = (InterfaceC) injector.getObject(InterfaceC.class);
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("exampleString"));
        System.out.println("\n************ End of Test 10 ************\n");
    }

    @Test
    public void test11GetFactoryC1InstanceSingleton() throws DependencyException{
        System.out.println("************ Test 11 ************");
        System.out.println("Tests if factoryD1 is correctly registered using Singleton and gets an instance of it.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
        InterfaceD d = (InterfaceD) injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
        System.out.println("\n************ End of Test 11 ************\n");
    }

    @Test(expected = DependencyException.class)
    public void test12RegisterSameConstantTwice() throws DependencyException{
        System.out.println("************ Test 12 ************");
        System.out.println("Tests if register the same constant twice.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(Integer.class, 42);
        System.out.println("\n************ End of Test 12 ************\n");
    }

    @Test (expected = DependencyException.class)
    public void test13RegisterSameFactoryTwice() throws DependencyException{
        System.out.println("************ Test 13 ************");
        System.out.println("Tests if register the same factory twice.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        System.out.println("\n************ End of Test 13 ************\n");
    }

    @Test
    public void test14checkgetObjectMethod() throws DependencyException{
        System.out.println("************ Test 14 ************");
        System.out.println("Checks if getObject() method returns different objects.\n");
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "exampleString");
        registerFactories();
        ImplementationA1 A1 = (ImplementationA1) injector.getObject(InterfaceA.class);
        ImplementationA1 A2 = (ImplementationA1) injector.getObject(InterfaceA.class);
        ImplementationB1 B1 = (ImplementationB1) injector.getObject(InterfaceB.class);
        ImplementationB1 B2 = (ImplementationB1) injector.getObject(InterfaceB.class);
        ImplementationC1 C1 = (ImplementationC1) injector.getObject(InterfaceC.class);
        ImplementationC1 C2 = (ImplementationC1) injector.getObject(InterfaceC.class);
        ImplementationD1 D1 = (ImplementationD1) injector.getObject(InterfaceD.class);
        ImplementationD1 D2 = (ImplementationD1) injector.getObject(InterfaceD.class);
        assertFalse(A1==A2);
        assertFalse(B1==B2);
        assertFalse(C1==C2);
        assertFalse(D1==D2);
        System.out.println("\n************ End of Test 14 ************\n");
    }



    private void registerFactories() throws DependencyException{
        injector.registerFactory(InterfaceA.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceC.class, new FactoryC1(), String.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);

    }

    private void registerSingletons() throws DependencyException{
        injector.registerSingleton(InterfaceA.class, new FactoryA1(), InterfaceB.class,InterfaceC.class);
        injector.registerSingleton(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerSingleton(InterfaceC.class, new FactoryC1(), String.class);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);

    }
}
