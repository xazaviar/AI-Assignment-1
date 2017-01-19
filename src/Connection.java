/**
 * This represents a single connection between two perceptatrons
 * @author Joseph Ryan
 * @class: Class CS4811
 * @date: 1/19/2017
 * @assignment: Artificial Neural Networks
 */
public class Connection {
    public Perceptatron src;    //The input perceptatron
    public Perceptatron dest;   //The output perceptatron
    public double weight;       //The weight of the connection
    
    /**
     * Creates the connection between the perceptatrons
     * and generates a random starting weight
     * @param src
     *          The input perceptatron
     * @param dest 
     *          The output perceptation
     */
    public Connection(Perceptatron src, Perceptatron dest){
        this.src = src;
        this.dest = dest;
        this.weight = Math.random();
        
        this.src.outputs.add(this);
        this.dest.inputs.add(this);
    }
}
