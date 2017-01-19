/**
 *
 * @author Joseph Ryan
 */
public class Connection {
    public Perceptatron src;
    public Perceptatron dest;
    public double weight;
    
    public Connection(Perceptatron src, Perceptatron dest){
        this.src = src;
        this.dest = dest;
        this.weight = Math.random();
        
        this.src.outputs.add(this);
        this.dest.inputs.add(this);
    }
}
