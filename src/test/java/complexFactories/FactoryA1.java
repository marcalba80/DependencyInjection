package complexFactories;

import Implementations.ImplementationA1;
import Implementations.ImplementationB1;
import Implementations.ImplementationC1;
import Interfaces.InterfaceB;
import Interfaces.InterfaceC;
import common.DependencyException;
import complex.Factory;

public class FactoryA1 implements Factory<ImplementationA1> {

    @Override
    public ImplementationA1 create(Object... parameters) throws DependencyException {
        InterfaceB b;
        InterfaceC c;

        try{
            b = (ImplementationB1) parameters[0];
            c = (ImplementationC1) parameters[1];
        }catch (ClassCastException | ArrayIndexOutOfBoundsException ex){
            System.err.println("ERROR: Something went wrong when trying to create a instance of ImplementationA1.");
            throw new DependencyException(ex);
        }
        return new ImplementationA1(b, c);
    }
}
