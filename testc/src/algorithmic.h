/*
 * algorithmic.h
 *
 *  Created on: 2015-6-24
 *      Author: chris
 */

#ifndef ALGORITHMIC_H_
#define ALGORITHMIC_H_
#include <iostream>
#include <math.h>
#include <string.h>
#include <stdio.h>

#include "data.h"
#include "xiuzheng.h"
#include "bengsongdata.h"

void Sort(unsigned char *pf, int n);
float GetFloatRound(float fVal, int bit0);
float GetAngleCor(float R, int iAng);
float GetSurfCor(float R, int nSurf);
float GetRegStrg(float Cardepth, float Modval, char Pumpflag,char ht_jd0,char ht_mian0);

#endif /* ALGORITHMIC_H_ */
