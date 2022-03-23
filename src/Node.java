import java.util.Objects;

public class Node {
    private Node prior;
    private double startdist;
    private double heuristic;
    private Point loc;


    public Node(Point loc, Node prior, double startdist, double heuristic) {
        this.prior = prior;
        this.startdist = startdist;
        this.heuristic = heuristic;
        this.loc = loc;
    }

    public Node getPrior() {
        return prior;
    }

    public double getStartdist() {// away from start
        return startdist;
    }

    public double getHeuristic() {// away from start
        return heuristic;
    }
    public double getTotaldist(){
        return  startdist + heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public Point getLoc() {
        return loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return startdist == node.startdist && Double.compare(node.heuristic, heuristic) == 0 && Objects.equals(prior, node.prior) && Objects.equals(loc, node.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prior, startdist, heuristic, loc);
    }

    @Override
    public String toString() {
        return "Node_point:" + loc;
    }
}
