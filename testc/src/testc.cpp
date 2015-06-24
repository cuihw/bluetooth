//============================================================================
// Name        : testc.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <math.h>
#include <string.h>
#include <stdio.h>

#include "algorithmic.h"

using namespace std;

int main() {
	cout << "!!!Hello World!!!" << endl; // prints !!!Hello World!!!

	// test for get float round.
	float round = GetFloatRound(4.336, 1);
	cout << "4.336 round = " << round << endl;

	round = GetFloatRound(4.335, 1);
	cout << "4.335 round = " << round << endl;

	round = GetFloatRound(4.355, 1);
	cout << "4.355 round = " << round << endl;

	round = GetFloatRound(4.352, 1);
	cout << "4.352 round = " << round << endl;

	round = GetFloatRound(4.351, 1);
	cout << "4.351 round = " << round << endl;



	return 0;
}

