#include <iostream>
#include <vector>
#include <atomic>
#include <thread>
#include <mutex>
#include <algorithm>

using namespace std;


void concurrentPartition(int numberOfThreads, int hashBits) {
    cout << "Concurrent Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 100;
    int blockSize = (int) numberOfTuples / numberOfThreads;

    vector<long> list(numberOfTuples);;
    vector<vector<long>> bucket(numberOfThreads, vector<long>(numberOfTuples / numberOfThreads));
    vector<vector<long>> partitions(hashBits, vector<long>(numberOfTuples / hashBits + 1));
    vector<mutex> locks(hashBits);
    vector<atomic<int>> counters(hashBits);
    vector<thread> threads(numberOfThreads);
    cout << "initialization completed" << endl;
    for (int i = 0; i < numberOfTuples; i++) {
        list[i] = i+1;
    }
    cout << "list created " << endl;
    for (int i = 0; i < numberOfThreads; i++) {
        for (int j = i * blockSize; j < (i+1) * blockSize; j++) {
            bucket[i][j - i * blockSize] = list[j];
        }
    }

    cout << "Buckets created " << endl;

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i] = thread([&, i](){
            cout << "Thread " << i << " started" << endl;
            for (int j = 0; j < bucket[i].size(); j++) {
                int partitionIndex = bucket[i][j] % hashBits;
                int position = counters[partitionIndex]++;
                partitions[partitionIndex][position] = bucket[i][j];
            }
        });
    }

    for (auto& thread : threads) {
        thread.join();
    }
    return;
}