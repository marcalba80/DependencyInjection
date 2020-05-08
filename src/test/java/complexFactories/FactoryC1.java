package complexFactories;

import Implementations.ImplementationC1;
import common.DependencyException;
import complex.Factory;

public class FactoryC1 implements Factory<ImplementationC1> {

    @Override
    public ImplementationC1 create(Object... parameters) throws DependencyException {
        String s;
        try {
            s = (String) parameters[0];
        }catch (ClassCastException | ArrayIndexOutOfBoundsException ex){
            System.err.println("ERROR: Something went wrong when trying to create a instance of ImplementationB1.");
            throw new DependencyException(ex);
        }
        return new ImplementationC1(s);
    }
}
