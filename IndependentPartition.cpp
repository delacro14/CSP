#include <iostream>
#include <vector>
#include <atomic>
#include <thread>
#include <mutex>
#include <ctime>
#include <ratio>
#include <chrono>

using namespace std;

double independentPartition(int numberOfThreads, int hashBits) {
    using namespace std::chrono;
    cout << "Independent  Partition " << numberOfThreads << " " << hashBits << endl;
    long numberOfTuples = 16777216;
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
    high_resolution_clock::time_point t1 = high_resolution_clock::now();

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
    high_resolution_clock::time_point t2 = high_resolution_clock::now();
    duration<double> time_span = duration_cast<duration<double>>(t2 - t1);
    cout << to_string(time_span.count()) << endl;

    return time_span.count();
}