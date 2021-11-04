LAZY = LazyDupList.java LazyInterface.java
LOCK = LockFreeDupList.java LockFreeInterface.java Window.java
OPTI = OptimisticDupList.java OptimisticInterface.java
TEST = TestLists.java ListTester.java BasicListTester.java BasicTestLists.java
JFLAGS = java -ea

.PHONY: opti lazy lock tester

all: tester opti lazy lock

tester:
	javac $(TEST)
opti:
	javac $(OPTI)
lazy:
	javac $(LAZY)
lock:
	javac $(LOCK)


run1: tester
	$(JFLAGS) TestLists 1

run10: tester
	$(JFLAGS) TestLists

run100: tester
	$(JFLAGS) TestLists 100

run1000: tester
	$(JFLAGS) TestLists 1000
run5: tester
	$(JFLAGS) TestLists 5



clean:
	rm -rf *.class
