package chap4;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {

        String[] genesisTransactions = {"satoshi send ivans 1000 bitcoin", "hal finney send 10 bitcoins to ivan"};
        Block genesisBlock = new Block(0, genesisTransactions);

        String[] block2Transactions = {"ivan send 10 bitcoins to satoshi", "satoshi sent 10 bitcoin to start"};
        Block block2 = new Block(genesisBlock.getBlockHash(), block2Transactions);

        System.out.println(genesisBlock.getBlockHash());
        System.out.println(block2.getBlockHash());

    }
}
