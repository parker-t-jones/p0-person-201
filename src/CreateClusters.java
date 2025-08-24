import java.io.*;
import java.util.*;
import com.google.gson.*;

/**
 * Create clusters from geographic (lat,long) data using
 * Density Based Spacial Cluster Algorithm (DBSCAN).  
 * 
 * @author Owen Astrachan
 */

public class CreateClusters {

    /**
     * Instance variables used in creating clusters. Variables
     * myEpsilon and myMinNeighbors define a cluster
     * 
     * myClusterMap is updated by calls to public methods
     * so that earch person/site is mapped to a number indicating
     * what cluster it is in. Cluster number zero is reserved for 
     * isolated/outlier sites, not part of any cluster. Actual
     * clusters start at one.
     */
    private double myEpsilon;
    private int myMinNeighbors;
    private Map<Person201,Integer> myClusterMap;

    /**
     * CreateCluster creates clusters with a minimal number of sites
     * in a cluster, and a minimal distance from a cluster's "center"
     * @param eps is the minimal epsilon for a site from a cluster's center
     * @param min is the minimal number of sites to be considered a cluster
     */
    public CreateClusters(double eps, int min){
        myEpsilon = eps;
        myMinNeighbors = min;
        myClusterMap = new HashMap<>();
    }

    /**
     * Using minimal number of sites and minimal epsilon create
     * clusters from the data in parameter people, return a list
     * of clusters, each element of the returned list is a cluster
     * @param people is the data from which clusters are created
     * @return a list of clusters. Each list that's an element of the
     * returned list is a cluster
     */
    public List<List<Person201>> createClusters(Person201[] people) {
        int clusterID = 1;
        Set<Person201> visited = new HashSet<>();

        /**
         * Consider each element of parameter people as a potential center
         * for a cluster. If it's a center, update information in
         * instance varaible myClusterMap. 
         */

        for(Person201 p : people){
            if (visited.contains(p)) continue; // already in a cluster

            visited.add(p);

            List<Person201> nearby = withinRegion(people, p);
            if (nearby.size() < myMinNeighbors){
                myClusterMap.put(p,0);   // outlier, not in a cluster
            }
            else {
                expandCluster(p,nearby,clusterID,people,visited);                         
                clusterID += 1;
            }
        }
        /**
         * Clusters were created by associating a number witih
         * each site/person. Create a new map that associates
         * each cluster number with a list of people in that cluster
         */
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

    /**
     * Given a center, and a cluster/list of those within myEpsilon of center, as well
     * as a set of those already processed, find all sites/people within myEpsilon
     * of anyone in the cluster, and for each site/person added, within myEpsilon of that
     * site/person. Cluster is expanded by associating all people in the cluster
     * with the specified clusterID
     * @param person is the center of the cluster 
     * @param nearby is list of those within myEpsilon of center
     * @param clusterID is the cluster ID of this cluster
     * @param people is the list of all sites/people to consider
     * @param visited is the list of sites/pepole already assigned to a cluster
     * 
     * @side-effect: alters instance variable myClusterMap to associate
     * all those in the cluster with parameter clusterID
     */
    private void expandCluster(Person201 person, List<Person201> nearby, 
                               int clusterID, Person201[] people,
                               Set<Person201> visited) {

        myClusterMap.put(person,clusterID);             // center has given ID
        List<Person201> list = new ArrayList<>(nearby);

        while (list.size() > 0){
            Person201 next = list.remove(list.size()-1); // check who is near this site/person
            if (! visited.contains(next)) {              // if they aren't already processed
                visited.add(next);
                
                List<Person201> close = withinRegion(people, next);
                if (close.size() >= myMinNeighbors){
                    list.addAll(close);
                }
            }
            // site/person next in the given cluster not already processed have clusterID
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
