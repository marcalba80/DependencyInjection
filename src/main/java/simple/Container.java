package simple;

import common.DependencyException;
import java.util.HashMap;
import java.util.Map;

public class Container implements Injector {

    private Map<String, Object> objectHashMap;
    private Map<String, Factory> factoryHashMap;
    private Map<String, Factory> singletonHashMap;
    private Map<String, Object> singletonObjectHashMap;
    private Map<String, String[]> dependenciesHashMap;


    public Container(){
        this.objectHashMap = new HashMap<>();
        this.factoryHashMap = new HashMap<>();
        this.singletonHashMap = new HashMap<>();
        this.singletonObjectHashMap = new HashMap<>();
        this.dependenciesHashMap = new HashMap<>();
    }

    @Override
    public void registerConstant(String name, Object value) throws DependencyException {
        if(!objectHashMap.containsKey(name)){
            objectHashMap.put(name, value);
        }
        else throw new DependencyException("Constant " + name + " is already registered");
    }

    @Override
    public void registerFactory(String name, Factory creator, String... parameters) throws DependencyException {
        if(!factoryHashMap.containsKey(name)){
            factoryHashMap.put(name, creator);
            dependenciesHashMap.put(name, parameters);
        }
        else throw new DependencyException("Factory " + name + " is already registered");
    }

    @Override
    public void registerSingleton(String name, Factory creator, String... parameters) throws DependencyException {
        if(!singletonHashMap.containsKey(name)){
            singletonHashMap.put(name, creator);
            dependenciesHashMap.put(name, parameters);
        }
        else throw new DependencyException("Singleton " + name + " is already registered");
    }

    @Override
    public Object getObject(String name) throws DependencyException {
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
            return objectHashMap.get(name);
        }
        else if (factoryHashMap.containsKey(name)){
            checkDependencies(name);
            return factoryHashMap.get(name).create(getObjectDependencies(name));
        }
        else if (singletonHashMap.containsKey(name)){
            if(!singletonObjectHashMap.containsKey(name)) {
                checkDependencies(name);
                return singletonObjectHashMap.put(name, singletonHashMap.get(name).create(getObjectDependencies(name)));
            }else
                return singletonObjectHashMap.get(name);
        }
        else{
            throw new DependencyException("Name no registered");
        }
    }

    private Object[] getObjectDependencies(String name){
        String[] dependencies = dependenciesHashMap.get(name);
        Object[] parameters = new Object[dependencies.length];
        for(int i = 0 ; i < dependencies.length ; i += 1){
            parameters[i] = objectHashMap.get(dependencies[i]);
        }
        return parameters;
    }

    private void checkDependencies(String name) throws DependencyException{
        for(String dependency: dependenciesHashMap.get(name)){
            if(!objectHashMap.containsKey(dependency) && !singletonObjectHashMap.containsKey(dependency))
                throw new DependencyException("Dependency missed");
        }
    }
}
