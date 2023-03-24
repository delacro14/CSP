#include <iostream>
#include "ConcurrentPartition.h"
#include "IndependentPartition.h"
#include <math.h>
#include <fstream>


using namespace std;

int main() {
    //concurrentPartition(4, 16);
    //independentPartition(4, 16);
    //set  of threads
    int threads[] = {1, 2, 4, 8, 16, 32};
    //set of hashbits
    int hashBits[] = {1, 2, 3, 4,5,6,7,8,9,10,11,12,13,14,15,16,17, 18,19, 20,21, 22,23,24,25,26,27,28,29,30,31,32};
    //for each thread
    //get length of threads
    int numberOfRuns = 10;
    for (int i = 0; i < 6; i++) {
        //for each hashbits
        for (int j = 0; j < 32; j++) {
            int numberOfMilis = 0;
            for (int k = 0; k < numberOfRuns; k++) {
                numberOfMilis += round(independentPartition(threads[i], pow(2, hashBits[j])) * 1000);
            }
            std::ofstream outfile;
            outfile.open("independentPartition.csv", std::ios_base::app); // append instead of overwrite
            outfile << to_string(threads[i]) + "," + to_string(hashBits[j]) + "," + to_string(numberOfMilis) + "," + to_string(16777216) << endl; 
            outfile.close();

            //run independentPartition
            //print the results
        }
    }

    for (int i = 0; i < 6; i++) {
        //for each hashbits
        for (int j = 0; j < 32; j++) {
            int numberOfMilis = 0;
            for (int k = 0; k < numberOfRuns; k++) {
                numberOfMilis += round(concurrentPartition(threads[i], pow(2, hashBits[j])) * 1000);
            }
            std::ofstream outfile;
            outfile.open("concurrentPartition.csv", std::ios_base::app); // append instead of overwrite
            outfile << to_string(threads[i]) + "," + to_string(hashBits[j]) + "," + to_string(numberOfMilis) + "," + to_string(16777216) << endl; 
            outfile.close();

            //run independentPartition
            //print the results
        }
    }



    return 0;
}