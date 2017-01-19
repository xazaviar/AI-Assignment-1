
/**
 * This program sets up a neural network and 
 * then trains the network to process and learn 
 * the given function
 * @author Joseph Ryan
 * @class: Class CS4811
 * @date: 1/19/2017
 * @assignment: Artificial Neural Networks
 */
public class Main {
    
    static Perceptatron[] network;
    
    /**
     * Runs the program
     * @param args 
     *          The training file and the number of nodes to produce
     */
    public static void main (String[] args){
        
        int n = 3;
        
        network = new Perceptatron[2+(n*(n+1)/2)];
        
        //Create all of the perceptatrons
        network[0] = new Perceptatron(0,0);
        network[1] = new Perceptatron(0,1);
        int layer = 1, node = 0, mark = n;
        for(int i = 2; i < network.length; i++, node++){
            if(node == mark){
                node = 0;
                layer++;
                mark--;
            }  
            network[i] = new Perceptatron(layer, node);
        }
        
        //Make Connections
        for(int i = 0; i < network.length-1; i++){   
            for(int l = i+1; l < network.length; l++){
                if(network[i].layer+1==network[l].layer)
                    new Connection(network[i],network[l]);
                else if(network[i].layer+2==network[l].layer)
                    break;
            }
        }
        
        //Output the network
        for(Perceptatron p: network){
            System.out.println(p.name());
            System.out.println(p.inputConnections());
            System.out.println(p.outputConnections());
        }
    }
    
}
