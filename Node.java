public class Node {
    private Point pt;
    private Node pr;
    private double f; //f + g
    private double h; //hueristic distance between pos and end
    private double g; //distance between start and current node, as more nodes are "found" this number is just added to
    public Node(Node pr, Point pt, double g, double h, double f) {
        this.pt = pt;
        this.pr = pr;
        this.g = g;
        this.h = h;
        this.f = f;
    }
    public Node getPr(){return pr;}
    public Point getPt(){return pt;}
    public void setG(double x){g = x;}
    public void setPr(Node x){pr = x;}
    public void setPt(Point x){pt = x;}
    public void setH(double x){h = x;}
    public void setF(double x){f = x;}
    public double getF(){return f;}
    public double getG(){return g;}
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(getClass() != other.getClass()){
            return false;
        }
        Node O = (Node)other;
        boolean result = true;
        if(pt==null){result = result && O.pt==null;}
        else{
            result = result && pt.equals(O.pt);
        }
        return result;
    }
}
