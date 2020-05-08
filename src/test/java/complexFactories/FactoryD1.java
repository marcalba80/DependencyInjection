package complexFactories;

import Implementations.ImplementationD1;
import common.DependencyException;
import complex.Factory;

public class FactoryD1 implements Factory<ImplementationD1> {
    @Override
    public ImplementationD1 create(Object... parameters) throws DependencyException {
        int i;

        try {
            i = (int) parameters[0];
        }catch (ClassCastException | ArrayIndexOutOfBoundsException ex){
            System.err.println("ERROR: Something went wrong when trying to create a instance of ImplementationB1.");
            throw new DependencyException(ex);
        }
        return new ImplementationD1(i);
    }
}
