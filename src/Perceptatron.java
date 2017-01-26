import java.math.BigDecimal;
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
    
    public double activation = 0;                               //This denotes if the node has activated
    public double deltaValue = 0;                               //This denotes the d value of this node
    
    //Reporting Variables
    public int layer, node;                                     //These variables are for marking each node
    public boolean outputNode = false;                          //This is for cleaner naming conventions
    
    /**
     * Create the perceptatron in a given spot
     * @param layer
     *          The perceptatron's layer
     * @param node 
     *          The perceptatron's node id
     * @param bias
     */
    public Perceptatron(int layer, int node, boolean bias){
        this.layer = layer;
        this.node = node;
        
        //Biased Connection
        if(bias)
            inputs.add(new Connection(null, this));
    }
    
    /**
     * This function calculates the activation of the neuron
     */
    public void activation(){
        double sum = 0;
        
        for(Connection in: inputs){
            if(in.src==null)
                sum+=in.weight;
            else
                sum+=in.src.activation*in.weight;
        }
        
        this.activation = sigmoid(sum);
    }
    
    public void threshold(){
        double sum = 0;
        
        for(Connection in: inputs){
            if(in.src==null)
                sum+=in.weight;
            else
                sum+=in.src.activation*in.weight;
        }

        this.activation = (sum>0?1:0);
    }
    
    /**
     * This function calculates the blame of a neuron
     * @param error 
     *          The amount to blame
     */
    public void blame(){
        for(Connection in: inputs)
            if(in.src!=null)
                in.src.deltaValue = in.src.activation * (1 - in.src.activation) * in.weight * this.deltaValue; 
    }
    
    /**
     * This function updates the weights with their new values
     * @param error
     *          The error outputted from final node
     * @param constant 
     *          The amount to change a weight
     */
    public void updateWeights(double constant){
        for(Connection in: inputs){
            in.weight += constant * (in.src==null?1:in.src.activation) * this.deltaValue;
        }
    }
    
    private double sigmoid(double x){
        return 1/(1+Math.pow(Math.E, -x));
    }
    
    //String formatted output
    //***************************************************************
    public String name(){
        if(this.layer==0)
            return "input"+this.node;
        else if(this.outputNode)
            return "output";
        return "L"+this.layer+"N"+this.node;
    }
    public String info(){
        //Make the weight have only 2 decimal places
        BigDecimal bd1 = new BigDecimal(this.activation);
        BigDecimal bd2 = new BigDecimal(this.deltaValue);
        bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
        bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
        return "\tact: "+bd1+" | d: "+bd2;
    }
    public String inputConnections(){
        String ret = "";
        for(Connection c: inputs){
            //Make the weight have only 2 decimal places
            BigDecimal bd = new BigDecimal(c.weight);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            ret += "\t IN : "+(c.src!=null?c.src.name():"bias")+" ["+bd.doubleValue()+"]\n";
        }
        return ret;
    }
    public String outputConnections(){
        String ret = "";
        for(Connection c: outputs){
            //Make the weight have only 2 decimal places
            BigDecimal bd = new BigDecimal(c.weight);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            ret += "\t OUT: "+c.dest.name()+" ["+bd.doubleValue()+"]\n";
        }
        return ret;
    }
}