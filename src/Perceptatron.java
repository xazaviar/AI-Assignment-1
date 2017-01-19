import java.util.ArrayList;

/**
 * The individual "neuron" in the neural network
 * @author Joseph Ryan
 * @class: Class CS4811
 * @date: 1/19/2017
 * @assignment: Artificial Neural Networks
 */
public class Perceptatron {
    public ArrayList<Connection> inputs = new ArrayList<>();    //The connections receiving inputs from
    public ArrayList<Connection> outputs = new ArrayList<>();   //The connections to output the signal too
    public Connection biasedSignal;                             //The connection that is always active
    public final int threshold = 0;                             //The threshold that needs to be broken before sending the signal
    
    //Reporting Variables
    public int layer, node;                                     //These variables are for marking each node
    
    public Perceptatron(int layer, int node){
        this.layer = layer;
        this.node = node;
    }
    
    public String name(){
        if(this.layer==0)
            return "input"+this.node;
        return "L"+this.layer+"N"+this.node;
    }
    
    public String inputConnections(){
        String ret = "";
        for(Connection c: inputs){
            ret += "\t IN: "+c.src.name()+" ["+c.weight+"]\n";
        }
        return ret;
    }
    
    public String outputConnections(){
        String ret = "";
        for(Connection c: outputs){
            ret += "\t OUT: "+c.dest.name()+" ["+c.weight+"]\n";
        }
        return ret;
    }
}