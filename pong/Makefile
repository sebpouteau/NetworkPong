all:
	javac $(shell find . -name \*.java)

run1: all
	java src.Main 7777 2
run2: all
	java src.Main 7778 10.0.203.7 7777


clean:
	rm $(shell find . -name \*.class)
