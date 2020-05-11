package simpleFactories;

import Implementations.ImplementationD1;
import common.DependencyException;
import simple.Factory;

public class FactoryD1 implements Factory {

    @Override
    public Object create(Object... parameters) throws DependencyException {
        int i;

        try {
            i = (int) parameters[0];
        }catch (ClassCastException | ArrayIndexOutOfBoundsException ex){
            System.err.println("ERROR: Something went wrong when trying to create a instance of ImplementationD1.");
            throw new DependencyException(ex);
        }
        return new ImplementationD1(i);
    }
}
