#include <iostream>
#include <vector>
#include <atomic>
#include <thread>
#include <mutex>

using namespace std;

void independentPartition(int numberOfThreads, int hashBits) {
    cout << "Independent  Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 3200000;
    int blockSize = (int) numberOfTuples / numberOfThreads;

    vector<long> list(numberOfTuples); //list of tuples
    vector<vector<long>> buckets(numberOfThreads, vector<long>(blockSize ));
    vector<thread> threads(numberOfThreads);

    //cout << "initialization completed" << endl;

    for (int i = 0; i < numberOfTuples; i++) {
        list[i] = static_cast<long>(i+1);
    }

    //cout << "list created " << endl;
    for (int i = 0; i < numberOfThreads; i++) {
        for (int j = i * blockSize; j < (i+1) * blockSize; j++) {
            buckets[i][j - i * blockSize] = list[j];
        }
    }

    //cout << "Buckets created " << endl;

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i] = thread([&, i](){
            vector<vector<long>> partitions(hashBits, vector<long>((buckets[i].size() / hashBits)+1));
            vector<int> counters(hashBits, 0);
            for (int j = 0; j < hashBits; j++) {
                counters[j] = 0;
            }
            //partition the bucket
            for (int j = 0; j < buckets[i].size(); j++) {
                int partitionIndex = static_cast<int>(buckets[i][j]) % hashBits;
                partitions[partitionIndex][counters[partitionIndex]] = buckets[i][j];
                counters[partitionIndex]++;
            }
        });
    }

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i].join();
    }
    return;
}