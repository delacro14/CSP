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
        vector<long> &list
) {
    vector<long> partitions;

    //partition the bucket
    for (int j = from; j < to; j++) {
        partitions.push_back(list[j]);
    }
}

double independentPartition(int numberOfThreads, int hashBits) {
    using namespace std::chrono;
    cout << "Independent  Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 16777216;
    int blockSize = numberOfTuples / numberOfThreads;

    vector<long> list(numberOfTuples); //list of tuples
    vector<thread> threads(numberOfThreads);

    //cout << "initialization completed" << endl;

    for (int i = 0; i < numberOfTuples; i++) {
        list[i] = i+1;
    }

    high_resolution_clock::time_point t1 = high_resolution_clock::now();

    for (int i = 0; i < numberOfThreads; i++) {
        int from = i * blockSize;
        int to = from + blockSize;
        if (i + 1 == numberOfThreads) {
            to = numberOfTuples;  // work the rest
        }
        threads[i] = thread(independentFunc, from, to, ref(list));
    }

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i].join();
    }
    high_resolution_clock::time_point t2 = high_resolution_clock::now();
    duration<double> time_span = duration_cast<duration<double>>(t2 - t1);
    cout << to_string(time_span.count()) << endl;

    return time_span.count();
}