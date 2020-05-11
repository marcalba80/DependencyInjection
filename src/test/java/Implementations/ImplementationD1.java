package Implementations;

import Interfaces.InterfaceD;

public class ImplementationD1  implements InterfaceD {
    private final int i;


    public ImplementationD1(int i) {
        this.i = i;
    }

    public int getI(){
        return this.i;
    }
}
