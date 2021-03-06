
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
    /**
     * Runs the program taking in the arguments n and training file
     * 
     * /prog n file_name
     * 
     * @param args 
     *          The training file and the number of nodes to produce
     */
    public static void main (String[] args){
        
        int[][] training = readTrainingFile(args[2]);
        double constant = Double.parseDouble(args[1]);
        Perceptatron[] network = makeNetwork2(Integer.parseInt(args[0])); //Create the network
        
        //Output the network
        System.out.println("---------------------------------------------------");
        System.out.println("Initial Network");
        System.out.println("---------------------------------------------------");
        for(Perceptatron p: network){
            System.out.println(p.name());
            System.out.print(p.inputConnections());
            System.out.print(p.outputConnections());
        }
        System.out.println("\n");
        
        //Begin Training the network with training file
        trainNetwork(network, training, constant);
        
        //Output the network
        System.out.println("---------------------------------------------------");
        System.out.println("Final Network");
        System.out.println("---------------------------------------------------");
        for(Perceptatron p: network){
            System.out.println(p.name());
            System.out.print(p.inputConnections());
            System.out.print(p.outputConnections());
        }
        System.out.println("\n");
        
        //Output the test grid
        System.out.println("---------------------------------------------------");
        System.out.println("Grid Output");
        System.out.println("---------------------------------------------------\n");
        testNetwork(network);
        System.out.println("\n");
        
    }
    
    /**
     * Reads in the training file and redirects the data
     * into a usable 2-d array
     * @param file
     *          The name of the file to read in
     * @return 
     *          A usable 2-d array
     */
    public static int[][] readTrainingFile(String file){
        int[][] training = null;
        try {
            int count = 0;
            String temp = "";
            Scanner s = new Scanner(new File(file));
            
            while(s.hasNext()){
                count++;
                temp+=s.next()+" ";
            }
            count/=3;
            
            s = new Scanner(temp);
            
            training = new int[count][3];
            for(int i = 0; i < count; i++){
                training[i][0] = s.nextInt();
                training[i][1] = s.nextInt();
                training[i][2] = s.nextInt();
            }   
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        return training;
    }
    
    /**
     * This function creates the network in a "tree" shape
     * with a given number of starting nodes 'n'
     * @param n
     *          The number of nodes in the first hidden layer
     * @return 
     *          A neural network
     */
    public static Perceptatron[] makeNetwork(int n){
        Perceptatron[] network = new Perceptatron[2+(n*(n+1)/2)];
        
        //Create all of the perceptatrons
        network[0] = new Perceptatron(0,0,false);
        network[1] = new Perceptatron(0,1,false);
        int layer = 1, node = 0, mark = n;
        for(int i = 2; i < network.length; i++, node++){
            if(node == mark){
                node = 0;
                layer++;
                mark--;
            }  
            network[i] = new Perceptatron(layer, node,true);
        }
        network[network.length-1].outputNode = true; //For naming conventions
        
        //Make Connections
        for(int i = 0; i < network.length-1; i++){   
            for(int l = i+1; l < network.length; l++){
                if(network[i].layer+1==network[l].layer)
                    new Connection(network[i],network[l]);
                else if(network[i].layer+2==network[l].layer)
                    break;
            }
        }
        
        return network;
    }
    
    /**
     * This function creates the network with a single 
     * hidden layer of n nodes
     * @param n
     *          The number of nodes in the hidden layer
     * @return 
     *          a feed forward neural network
     */
    public static Perceptatron[] makeNetwork2(int n){
        Perceptatron[] network = new Perceptatron[3+n];
        
        //Create all of the perceptatrons
        network[0] = new Perceptatron(0,0,false);
        network[1] = new Perceptatron(0,1,false);
        int layer = 1, node = 0;
        for(int i = 2; i < network.length-1; i++, node++){
            network[i] = new Perceptatron(layer,node,true);
        }
        network[network.length-1] = new Perceptatron(2,0,true);
        network[network.length-1].outputNode = true; //For naming conventions
        
        //Make Connections
        for(int i = 0; i < network.length-1; i++){   
            for(int l = i+1; l < network.length; l++){
                if(network[i].layer+1==network[l].layer)
                    new Connection(network[i],network[l]);
                else if(network[i].layer+2==network[l].layer)
                    break;
            }
        }
        
        return network;
    }
    
    /**
     * This function trains the neural network using the given training,
     * network, and constant adjust value
     * @param network
     *          The network to train
     * @param training
     *          The training to give to the network
     * @param constant 
     *          The constant to adjust the weights by
     */
    public static void trainNetwork(Perceptatron[] network, int[][] training, double constant){
        int correct = 0, count = 0;
        
        int report = -1;
        
        //loop until all training outputs are correct
        do{
            count++;
            //For each training example
            for(int i = 0; i < training.length; i++){
                //Set initial input nodes
                network[0].activation = training[i][0];
                network[1].activation = training[i][1];
                
                //Feed all values forward
                for(int n = 2; n < network.length; n++){
                    network[n].activation();
                }
                
                //Check output and calc error
                double act = network[network.length-1].activation;
                network[network.length-1].deltaValue = act * (1-act) * (training[i][2]-act);
                
                if(i==report || report == -2)
                    System.out.println("Network output -> "+act+" | error = "+network[network.length-1].deltaValue+"\n");
                
                //Propagate the delta backward from output layer to input layer
                for(int n = network.length-1; n > 1; n--){
                    if(network[n].layer==1)
                        break;
                    network[n].blame();
                }
                
                //Update every weight in the network
                for(int n = network.length-1; n > 1; n--){
                    network[n].updateWeights(constant);
                    if(i==report || report == -2){
                        System.out.println(network[n].name()+network[n].info());
                        System.out.print(network[n].inputConnections());
                    }
                }
            }
            
            correct = testExamples(count,network,training);
        } while (correct < training.length && count < 10000000);
        
    }
    
    /**
     * Test the current network with the training tests
     * @param iter
     *          The current network iteration
     * @param network
     *          The network to test
     * @param training
     *          The training cases to test against
     * @return 
     *          The number of tests that are correct
     */
    public static int testExamples(int iter, Perceptatron[] network, int[][] training){
        int correct = 0;
        
        System.out.println("\n---------------------------------------------------");
        System.out.println("Iteration: "+iter);
        System.out.println("---------------------------------------------------");
        System.out.println("INCORRECT TESTS: \n");
        
        for(int t = 0; t < training.length; t++){
            //Set initial input nodes
            network[0].activation = training[t][0];
            network[1].activation = training[t][1];

            //Feed all values forward
            for(int n = 2; n < network.length; n++){
                network[n].threshold();
            }

            //Check output
            if(((int)network[network.length-1].activation)!=training[t][2]){
//                System.out.println("Test "+t+" | x1 = "+training[t][0]+", x2 = "+training[t][1]+", out = "+training[t][2]);
//                System.out.println("\toutput: "+(int)network[network.length-1].activation+" | expected: "+training[t][2]+"\n");
            }
            correct += ((int)network[network.length-1].activation==training[t][2]?1:0);

            //Clean up nodal activations
            for(Perceptatron p: network)
                p.activation = 0;
        }
        
        System.out.println("Tests Passed: "+correct+"/"+training.length);
        
        return correct;
    }
    
    /**
     * This function takes the trained network and reports the output
     * for a given set of values
     * @param network 
     *          The network to test
     */
    public static void testNetwork(Perceptatron[] network){
        double xMax = 50, yMax = 50, inc = 1;
        
        for(double y = yMax; y >= 0; y-=inc){
            for(double x = 0; x <= xMax; x+=inc){
                //Set initial input nodes
                network[0].activation = x;
                network[1].activation = y;
                
                //Feed all values forward
                for(int n = 2; n < network.length; n++){
                    network[n].threshold();
                }
                
                //Check output
                //Make the weight have only 2 decimal places
                BigDecimal bd = new BigDecimal(network[network.length-1].activation);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                System.out.print(""+(int)network[network.length-1].activation+"  ");
                
                //Clean up nodal activations
                for(Perceptatron p: network)
                    p.activation = 0;
            }
            System.out.println();
        }
    }
    
}
