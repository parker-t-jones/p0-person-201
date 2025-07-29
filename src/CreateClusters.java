import java.io.*;
import java.util.*;
import com.google.gson.*;

/**
 * Create clusters from geographic (lat,long) data using
 * Density Based Spacial Cluster Algorithm (DBSCAN) 
 * @author Owen Astrachan
 */

public class CreateClusters {
    private double myEpsilon;
    private int myMinNeighbors;
    private Map<Person201,Integer> myClusterMap;

    public CreateClusters(double eps, int min){
        myEpsilon = eps;
        myMinNeighbors = min;
        myClusterMap = new HashMap<>();
    }

    public List<List<Person201>> createClusters(Person201[] people) {
        int clusterID = 1;
        Set<Person201> visited = new HashSet<>();

        for(Person201 p : people){
            if (visited.contains(p)) continue;
            visited.add(p);

            List<Person201> nearby = withinRegion(people, p);
            if (nearby.size() < myMinNeighbors){
                myClusterMap.put(p,0);
            }
            else {
                expandCluster(p,nearby,clusterID,people,visited);                         
                clusterID += 1;
            }
        }
        Map<Integer, List<Person201>> clusters = new HashMap<>();
        for(Person201 p : people){
            if (myClusterMap.containsKey(p)){
                int id = myClusterMap.get(p);
                if (!clusters.containsKey(id)){
                    clusters.put(id,new ArrayList<>());
                }
                clusters.get(id).add(p);
            }
        }
        List<List<Person201>> ret = new ArrayList<>(clusters.values());
        return ret;
    }

    private void expandCluster(Person201 person, List<Person201> nearby, 
                               int clusterID, Person201[] people,
                               Set<Person201> visited) {
        myClusterMap.put(person,clusterID);
        Queue<Person201> q = new LinkedList<>(nearby);

        while (! q.isEmpty()){
            Person201 next = q.remove();
            if (! visited.contains(next)) {
                visited.add(next);
                
                List<Person201> list = withinRegion(people, next);
                if (list.size() >= myMinNeighbors){
                    q.addAll(list);
                }
            }
            if (! myClusterMap.containsKey(next) || myClusterMap.get(next) == 0){
                myClusterMap.put(next,clusterID);
            }          
        }
    }

    /**
     * Return list of all elements of list that are within myEpsilon (distance)
     * of center.
     * @param list is a list of Person201 objects
     * @param center is the anchor/center of region returned
     * @return all elements of list withing myEpsilon of center
     */
    private List<Person201> withinRegion(Person201[] list, Person201 center){
        List<Person201> ret = new ArrayList<>();
        for(Person201 p : list){
            if (Person201Utilities.distance(center, p) <= myEpsilon){
                ret.add(p);
            }
        }
        return ret;
    }

    /**
     * Exports data in JSON format for visualization. Gson JSON format
     * is like a map (keys,values) so map created for each object
     * @param clusters is the clusters written in JSON format
     * @param fname is name of file being written
     * @throws IOException if writing to file fails
     */
    public void exportJSON(List<List<Person201>> clusters, String fname) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Map<String, Object>> output = new ArrayList<>();

        int clusterID = 1;
        for(int k=1; k < clusters.size(); k++) {
            for(Person201 p : clusters.get(k)) {
                Map<String,Object> entry = new HashMap<>();
                entry.put("cluster",clusterID);
                entry.put("name",p.name());
                entry.put("latitude",p.latitude());
                entry.put("longitude",p.longitude());
                output.add(entry);
            }
            clusterID += 1;         
        }
        try(Writer writer = new FileWriter(fname)){
            gson.toJson(output,writer);
        }
    }

    public static void main(String[] args) throws IOException {
        double clusterWidth = 30.0; // km
        int minSize = 4;
        String filename = "data/bigdata.txt";
        CreateClusters cc = new CreateClusters(clusterWidth,minSize);
        Person201[] people = Person201Utilities.readFile(filename);
        System.out.printf("read data for %d people\n",people.length);

        List<List<Person201>> list = cc.createClusters(people);
        int total = 0;
        for(int k=1; k < list.size(); k++){
            System.out.printf("Cluster %d, size = %d:\n",k,list.get(k).size());
            total += list.get(k).size();
        }
        System.out.printf("# in clusters = %d\n",total);
        String visualizeFile = "data.json"; 
        cc.exportJSON(list, visualizeFile);
    }
}
