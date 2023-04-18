#include <iostream>
#include <vector>
#include <atomic>
#include <thread>
#include <mutex>
#include <ctime>
#include <ratio>
#include <chrono>

using namespace std;

void independentFunc(
        int from,
        int to,
        int hashBits,
        vector<long> &list,
        vector<vector<long>*> &partitions
) {
//    partitions.resize(hashBits, nullptr);
//    cout << "hashBits: " << hashBits << endl;
//    cout << "partitions size: " << partitions.size() << endl;
//    cout << "(to - from)/hashBits: " << (to - from)/hashBits << endl;
//    vector<vector<long>*> partitions(hashBits, nullptr);
    for (int j = 0; j < hashBits; j++) {
        partitions[j] = new vector<long>();
        partitions[j]->reserve((to - from)/hashBits);
    }

    //partition the bucket
    for (int j = from; j < to; j++) {
        int partitionIndex = list[j] % hashBits;
        partitions[partitionIndex]->push_back(list[j]);
    }
}

double independentPartition(int numberOfThreads, int hashBits) {
    using namespace std::chrono;
    cout << "Independent  Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 16777216;
    int blockSize = numberOfTuples / numberOfThreads;

    vector<long> list(numberOfTuples); //list of tuples
    vector<thread> threads(numberOfThreads);
    vector<vector<vector<long>*>>finalOutput;

    //cout << "initialization completed" << endl;

    for (int i = 0; i < numberOfTuples; i++) {
        list[i] = i+1;
    }

    high_resolution_clock::time_point t1 = high_resolution_clock::now();
    finalOutput = vector<vector<vector<long>*>>(numberOfThreads, vector<vector<long>*>(hashBits, nullptr));

    for (int i = 0; i < numberOfThreads; i++) {
        int from = i * blockSize;
        int to = from + blockSize;
        if (i + 1 == numberOfThreads) {
            to = numberOfTuples;  // work the rest
        }
        threads[i] = thread(independentFunc, from, to, hashBits, ref(list), ref(finalOutput[i]));
    }

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i].join();
    }
    high_resolution_clock::time_point t2 = high_resolution_clock::now();
    duration<double> time_span = duration_cast<duration<double>>(t2 - t1);
    cout << to_string(time_span.count()) << endl;
    for (int i = 0; i < numberOfThreads; i++) {
        for (int j = 0; j < hashBits; j++) {
            delete finalOutput[i][j];
        }
    }
    return time_span.count();
}