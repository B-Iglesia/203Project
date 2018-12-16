final class Point
{
   public final int x;
   public int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }
   public int getX(){
       return this.x;
   }
   public int getY(){
       return this.y;
   }
   public void setY(int y){this.y = y;}
   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).getX() == this.getX() &&
         ((Point)other).getY() == this.getY();
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }
    public  boolean adjacent( Point p2)
    {
        return (this.getX() == p2.getX() && Math.abs(this.getY() - p2.getY()) == 1) ||
                (this.getY() == p2.getY() && Math.abs(this.getX() - p2.getX()) == 1);
    }

    public int distanceSquared(Point p2)
    {
        int deltaX = this.getX() - p2.getX();
        int deltaY = this.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

}
