import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(final Point start, final Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Node> open = new ArrayList<>();
        List<Point> closed = new ArrayList<>();
        Node first = new Node(null, start,0, start.distanceSquared(end),start.distanceSquared(end));
        open.add(first);
        Comparator<Node> f_comp = (Node1,Node2) -> { //prriority queue set up
            Double f1Node = Node1.getF();
            Double f2Node = Node2.getF();
            return f1Node.compareTo(f2Node);
        };
        boolean done = false;
        boolean stuck = false;
        List<Point> neighbors = potentialNeighbors.apply(start)
                .filter(canPassThrough).collect(Collectors.toList());
        if(neighbors.size() ==0){
            stuck = true;
        }
        while(!done && !open.isEmpty() && !stuck){
            Node current = open.get(0);
            open.remove(current);
            closed.add(current.getPt());
            List<Point> c_neighbors = potentialNeighbors.apply(current.getPt())
                .filter(canPassThrough).collect(Collectors.toList());
            for(Point neighbor : c_neighbors){
                if(!closed.contains(neighbor)){
                    double tent_g = current.getPt().distanceSquared(neighbor);
                    double tent_h = neighbor.distanceSquared(end);
                    double tent_f = tent_g + tent_h;
                    Node node = new Node(current,neighbor,tent_g,tent_h,tent_f);
                    if(!open.contains(node)){
                        open.add(node);
                    }
                    if(open.contains(node)){
                        int idx = open.indexOf(node);
                        if(tent_g<open.get(idx).getG()){
                            //open.remove(idx);
                            //open.add(idx,node);
                            open.get(idx).setPr(current);
                            open.get(idx).setG(tent_g);
                            open.get(idx).setF(tent_f);
                            open.get(idx).setH(tent_h);
                        }
                    }
                    if(withinReach.test(neighbor,end)){
                        done = true;
                        //Node last = new Node(node,end,0,0,0);
                        //open.add(last);
                    }
                }
            }
            Collections.sort(open, f_comp);
        }
        List<Point> path = new ArrayList<>();
        if(done){
            Node temp = open.get(0);
            while(temp.getPt() != start){
                path.add(temp.getPt());
                temp=temp.getPr();
            }
            Collections.reverse(path);
        }
        else{
            path.add(start);
        }
        System.out.println(path);
        return path;
    }
}
