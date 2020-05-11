package complex;

import common.DependencyException;

import java.util.HashMap;

public class Container implements Injector {

    private boolean REGISTERED = true;
    private HashMap<Class<?>, Object> objectHashMap;
    private HashMap<Class<?>, Factory<?>> factoryHashMap;
    private HashMap<Class<?>, Factory<?>> singletonHashMap;
    private HashMap<Class<?>, Object> singletonObjectHashMap;
    private HashMap<Class<?>, Class<?>[]> dependenciesHashMap;

    public Container(){
        this.objectHashMap = new HashMap<>();
        this.factoryHashMap = new HashMap<>();
        this.singletonHashMap = new HashMap<>();
        this.singletonObjectHashMap = new HashMap<>();
        this.dependenciesHashMap = new HashMap<>();
    }

    @Override
    public <E> void registerConstant(Class<E> name, E value) throws DependencyException {
        if(!objectHashMap.containsKey(name)){
            objectHashMap.put(name, value);
        }
        else throw new DependencyException("Constant " + name + " is already registered");
    }

    @Override
    public <E> void registerFactory(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if(!factoryHashMap.containsKey(name)){
            factoryHashMap.put(name, creator);
            dependenciesHashMap.put(name, parameters);
        }
        else throw new DependencyException("Factory " + name + " is already registered");
    }

    @Override
    public <E> void registerSingleton(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if(!singletonHashMap.containsKey(name)){
            singletonHashMap.put(name, creator);
            dependenciesHashMap.put(name, parameters);
        }
        else throw new DependencyException("Singleton " + name + " is already registered");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E getObject(Class<E> name) throws DependencyException {
        if(objectHashMap.containsKey(name) && factoryHashMap.containsKey(name)){
            throw new DependencyException("Key duplicated");
        }
        if(objectHashMap.containsKey(name) && singletonHashMap.containsKey(name)){
            throw new DependencyException("Key duplicated");
        }
        if(factoryHashMap.containsKey(name) && singletonHashMap.containsKey(name)){
            throw new DependencyException("Key duplicated");
        }
        if(objectHashMap.containsKey(name)){
            return (E) objectHashMap.get(name);
        }
        else if (factoryHashMap.containsKey(name)){
            checkDependencies(name);
            return (E) factoryHashMap.get(name).create(getObjectDependencies(name));
        }
        else if (singletonHashMap.containsKey(name)){
            if(!singletonObjectHashMap.containsKey(name)) {
                checkDependencies(name);
                return (E) singletonObjectHashMap.put(name, singletonHashMap.get(name).create(getObjectDependencies(name)));
            }else
                return (E) singletonObjectHashMap.get(name);
        }
        else{
            throw new DependencyException("Name no registered");
        }
    }

    @SuppressWarnings("unchecked")
    private <E> Object[] getObjectDependencies(Class<E> name){
        Class<E>[] dependencies = (Class<E>[]) dependenciesHashMap.get(name);
        Object[] parameters = new Object[dependencies.length];
        for(int i = 0 ; i < dependencies.length ; i += 1){
            parameters[i] = objectHashMap.get(dependencies[i]);
        }
        return parameters;
    }

    @SuppressWarnings("unchecked")
    private <E> void checkDependencies(Class<E> name) throws DependencyException{
        for(Class<E> dependency: (Class<E>[]) dependenciesHashMap.get(name)){
            if(!objectHashMap.containsKey(dependency)) throw new DependencyException("Dependency missed");
        }
    }
}
