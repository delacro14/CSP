#include <iostream>
#include "ConcurrentPartition.h"
#include "IndependentPartition.h"

using namespace std;

int main() {
    concurrentPartition(4, 16);
    independentPartition(4, 16);

    return 0;
}