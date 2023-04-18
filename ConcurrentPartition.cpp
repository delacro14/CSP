#include <iostream>
#include <vector>
#include <atomic>
#include <thread>
#include <mutex>
#include <ctime>
#include <ratio>
#include <chrono>

using namespace std;

void concurrentFunc(
        int from,
        int to,
        vector<long> &list,
        vector<vector<long>> &partitions,
        int hashBits,
        vector<mutex>  &locks
        ){
    for (int j = from; j < to; j++) {
        int partitionIndex = list[j] % hashBits;
        locks[partitionIndex].lock();
        partitions[partitionIndex].push_back(list[j]);
        locks[partitionIndex].unlock();
    }
}

double concurrentPartition(int numberOfThreads, int hashBits) {
    using namespace std::chrono;
    cout << "Concurrent Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 16777216;
    int blockSize = numberOfTuples / numberOfThreads;

    vector<long> list(numberOfTuples);;
    vector<mutex> locks(hashBits);
    vector<thread> threads(numberOfThreads);
    for (int i = 0; i < numberOfTuples; i++) {
        list[i] = i+1;
    }

    high_resolution_clock::time_point t1 = high_resolution_clock::now();
    vector<vector<long>> partitions(hashBits);

    for (int i = 0; i < numberOfThreads; i++) {
        int from = i * blockSize;
        int to = from + blockSize;
        if (i + 1 == numberOfThreads) {
            to = numberOfTuples;  // work the rest
        }
        threads[i] = thread(concurrentFunc, from, to, ref(list), ref(partitions), hashBits, ref(locks));
    }

    for (int i = 0; i < numberOfThreads; i++) {
        threads[i].join();
    }
    high_resolution_clock::time_point t2 = high_resolution_clock::now();
    duration<double> time_span = duration_cast<duration<double>>(t2 - t1);
    cout << to_string(time_span.count()) << endl;

    return time_span.count();
}