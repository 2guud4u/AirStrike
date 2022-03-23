import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        int counter = 0;
        Boolean found = false;
        List<Point> path = new LinkedList<Point>();
        Node startN = new Node(start, null, 0,calcDist(start, end) );
        HashMap<Point, Node> closedlist = new HashMap<>();// searched list of points
        PriorityQueue<Node> openlist = new PriorityQueue<>(Comparator.comparing(Node::getTotaldist));
        HashMap<Point, Node> openlist2 = new HashMap<>();
        Consumer<Node> swapper = n ->
        {
            if (openlist2.containsKey(n.getLoc())){
                openlist2.replace(n.getLoc(),n);
            }
            else {
                openlist2.put(n.getLoc(), n);
            }
        };
        //2.
        openlist.add(startN);
        openlist2.put(startN.getLoc(), startN);
        Node endnode = null;
        //3.
        while(!found) {
            counter++;
            Node Current = openlist.remove();
            if (Current == null || counter == 500){
                return path;
            }
            openlist2.remove(Current.getLoc(), Current); // actual list
            closedlist.put(Current.getLoc(), Current); // put current into closed list
            List<Node> neigh = potentialNeighbors.apply(Current.getLoc())
                    .filter(canPassThrough)
                    .filter(s -> (openlist2.containsKey(s)
                            ? openlist2.get(s).getStartdist() > (Current.getStartdist() + 1): true))// if old is smaller than new return true
                    .filter(s -> s != start)// make sure it is not start;
                    .filter(s -> !closedlist.containsKey(s))
                    .map(s -> new Node(s, Current, Current.getStartdist() + 1, calcDist(s,end))).collect(Collectors.toList());


            neigh.stream().forEach(swapper);//if key is dup, replace it

            neigh.stream().forEach(s -> openlist.add(s));
            // if heap is size 0
            if (openlist.size() == 0){
                return path;
            }
            endnode = Current;
            if (withinReach.test(Current.getLoc(), end)){
                break;
            }
        }
        // get the path after end point found
        while(endnode != null && !(endnode.getPrior() == null))
        {
            path.add(endnode.getLoc());
            endnode = endnode.getPrior();
        }
        Collections.reverse(path);
        return path;
    }

    public double calcDist(Point p1, Point p2){

        return Math.sqrt(Math.pow((p1.x- p2.x),2)+Math.pow((p1.y-p2.y),2));
    }



}
