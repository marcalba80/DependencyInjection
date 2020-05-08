package simple;

import com.sun.org.apache.regexp.internal.RE;
import common.DependencyException;

import java.rmi.registry.Registry;
import java.util.HashMap;

public class Container implements Injector {

    private boolean REGISTERED = true;
    private HashMap<String,Object> objectHashMap;
    private HashMap<String, simple.Factory> factoryHashMap;
    private HashMap<String, String[]> dependenciesHashMap;
    private HashMap<String, simple.Factory> singletonHashMap;



    public Container(){
        this.objectHashMap = new HashMap<String, Object>();
        this.factoryHashMap = new HashMap<String, Factory>();
        this.dependenciesHashMap = new HashMap<String, String[]>();
        this.singletonHashMap = new HashMap<String, Factory>();

    }


    public void registerConstant(String name, Object value) throws DependencyException {
        if (this.objectHashMap.containsKey(name)){
            if (REGISTERED) System.err.println("ERROR: Constant " + name + " is already registered");
            throw new DependencyException("Constant " + name + " is already registered");
        }else{
            this.objectHashMap.put(name, value);
            if (REGISTERED)System.out.println("Name " + name + " registered with value " + value);
        }
    }

    public void registerFactory(String name, Factory creator, String... parameters) throws DependencyException {
        if (this.factoryHashMap.containsKey(name)){
            if (REGISTERED)System.err.println("ERROR: Factory " + name + " is already registered");
            throw new DependencyException("Factory " + name + " is already registered");
        }else{
            if (REGISTERED)System.out.println("Registering factory " + name);
            this.factoryHashMap.put(name, creator);
            if (REGISTERED) System.out.println("Factory " + name + " successfully registered");
            if (REGISTERED)System.out.println("Registering factory dependencies for factory " + name);
            this.dependenciesHashMap.put(name, parameters);
            if (REGISTERED)System.out.println("Successfully registered dependencies for factory " + name);

        }
    }

    public void registerSingleton(String name, Factory creator, String... parameters) throws DependencyException {
        if (this.singletonHashMap.containsKey(name)){
            if (REGISTERED) System.err.println("ERROR: Singleton " + name + " is already registered");
            throw new DependencyException("Singleton " + name + " is already registered");
        }else{
            if (REGISTERED)System.out.println("Registering singleton " + name);
            this.singletonHashMap.put(name, creator);
            if (REGISTERED) System.out.println("Singleton factory " + name + " successfully registered");
            if (REGISTERED)System.out.println("Registering factory dependencies for factory " + name);
            this.dependenciesHashMap.put(name, parameters);
            if (REGISTERED)System.out.println("Successfully registered dependencies for factory " + name);
        }
    }


    public Object getObject(String name) throws DependencyException {
        if (this.objectHashMap.containsKey(name)){
            return this.objectHashMap.get(name);
        }else if (this.factoryHashMap.containsKey(name)){
            return this.createFactory(name);
        }else if (this.singletonHashMap.containsKey(name)){
            return this.createSingletonFactory(name);
        }
        else {
            throw new DependencyException(name + " has not been registered");
        }
    }


    private Object createFactory(String name) throws DependencyException {

        try{
            Factory creator;
            creator = this.factoryHashMap.get(name);
            Object[] object = new Object[this.dependenciesHashMap.get(name).length];
            for (int i=0; i<this.dependenciesHashMap.get(name).length; i++){
                object[i] = this.getObject(this.dependenciesHashMap.get(name)[i]);
            }
            return creator.create(object);
        }catch(DependencyException ex){
            if (REGISTERED) System.err.println("ERROR: Factory " + name + " can't be created");
            throw new DependencyException(ex);
        }
    }

    private Object createSingletonFactory(String name) throws DependencyException {

        try{
            Factory creator;
            creator = this.singletonHashMap.get(name);
            Object[] object = new Object[this.dependenciesHashMap.get(name).length];
            for (int i=0; i<this.dependenciesHashMap.get(name).length; i++){
                object[i] = this.getObject(this.dependenciesHashMap.get(name)[i]);
            }
            return creator.create(object);
        }catch(DependencyException ex){
            if (REGISTERED) System.err.println("ERROR: Factory " + name + " can't be created");
            throw new DependencyException(ex);
        }
    }
}
