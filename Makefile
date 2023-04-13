# Compiler options
CC = g++
CFLAGS = -Wall -Wextra -std=c++11 -g

# Linker options
LDFLAGS = -pthread

# Target executable
TARGET = CSP_in_c

# Source files
SRCS = main.cpp ConcurrentPartition.cpp IndependentPartition.cpp

# Header files
HDRS = ConcurrentPartition.h IndependentPartition.h

# Object files
OBJS = $(SRCS:.cpp=.o)

# Rule to build object files from source files
%.o: %.cpp $(HDRS)
	$(CC) $(CFLAGS) -c $< -o $@

# Rule to build the target executable
$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $@

# Rule to clean up object files and the target executable
clean:
	rm -f $(OBJS) $(TARGET)
