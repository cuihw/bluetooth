/******************************************************
函数：将测试得到的16个 测点排序，去掉3个最大值，3个最小值，得到平均值
参数：pf：保存16个回弹数据的数组
      n：进行排序的数据的个数
返回值：0
***********************************************************/
void Sort(unsigned char *pf, int n)//排序
{
	int i,j;
	unsigned char ft;
	for(i=0; i<n; i++)
	{
		for(j=0; j<n-1; j++)
		{
			if(pf[j] < pf[j+1])
			{
				ft = pf[j];
				pf[j] = pf[j+1];
				pf[j+1] = ft;
			}
		}
	}
}

/******************************************************
函数：四舍六入，奇进偶不进
参数：fVal：实数
      bit0：有效位数
返回值：0
***********************************************************/
float GetFloatRound(float fVal, int bit0)
{
	float val;
	int i;
	char strTmp[20];
	char tmpStr;
	int nTmp;
	//int ival;

	val = fVal;
	
	for(i=0; i<=bit0; i++)
		val = val * 10.F;
	
	ftoa(val,0,strTmp);
	
	tmpStr=strTmp[strlen(strTmp)-1];
	
	nTmp=tmpStr-48;
	if(nTmp==5)
	{
		tmpStr=strTmp[strlen(strTmp)-2];
		if(tmpStr%2!=0)
			val=(float)(int)(fabs(val)+5);
		else
			val=(float)(int)(fabs(val)-5);
	}
	else if(nTmp<5)
	{
		val=(float)(int)(fabs(val)-nTmp);
	}
	else
		val=(float)(int)(fabs(val)-nTmp+10);
	
	if(fVal<0) val=-val;
	
	for(i=0; i<=bit0; i++)
		val = val / 10.F;
	return val;
}

/******************************************************
函数：对推定值进行角度修正
参数：R:推定值
      iAng：角度对应在数组中的位置
返回值：修正后的推定值
***********************************************************/
float GetAngleCor(float R, int iAng) 
{
	float fRCor;
	//int iR;
	int i;
	if(R < MIN_RM)
		R = MIN_RM;
	else if(R > 50.F)
		R = 50.F;
	
	fRCor=0.0F;
	//iR = 0;
	for(i=1;i<R_ANG_NUM;i++)
	{
		if(R==fAveR[i])
		{
			fRCor=TestAngCor[i][iAng];
			break;
		}
		else if(R<fAveR[i])
		{
			fRCor=TestAngCor[i-1][iAng]+(R-fAveR[i-1])*(TestAngCor[i][iAng]-TestAngCor[i-1][iAng])/(fAveR[i]-fAveR[i-1]);
			break;
		}
	}
	fRCor=GetFloatRound(fRCor, 1);
	return fRCor;
}
/******************************************************
函数：测强测试面修正插值
参数：R:推定值
      nSurf：是侧面、表面、底面
返回值：修正后的推定值
***********************************************************/
float GetSurfCor(float R, int nSurf)  
{
	float fRCor;
	int i;
	if(R < MIN_RM)
		R = MIN_RM;
	else if(R > 50.F)
		R = 50.F;
	
	fRCor = 0.F;
	if(nSurf > 2)
		return fRCor;
	
	for(i=1;i<R_SURF_NUM;i++)
	{
		fRCor=TestSurfCor[i-1][nSurf]+(R-fAveR[i-1])*(TestSurfCor[i][nSurf]-TestSurfCor[i-1][nSurf])/(fAveR[i]-fAveR[i-1]);
	}
	fRCor=GetFloatRound(fRCor, 1);
	//	ftoa(fRCor,1,str0);	//测试
	//	for(i=0;i<5;i++)
	//	send_uart(str0[i]);
	return fRCor;
}
/******************************************************
函数：计算强度值
参数：Modval：回弹值得平均值
      Cardepth：炭化深度平均值
	  Pumpflag：泵送非泵送
      ht_jd0：角度修正
	  ht_mian0：测试面修正
返回值：修正后的推定值
***********************************************************/
float GetRegStrg(float Cardepth, float Modval, char Pumpflag,char ht_jd0,char ht_mian0)
{
	float fVal,fR2,fR,fCor,fR1,fC;
	
	int nC,i,nR;
	
	fVal = 0.F;
	fR =Modval;// GetAvarage();
	fR2 = GetFloatRound(fR, 1);//先四舍五入
	
	
	fCor = GetAngleCor(fR2,ht_jd0);//角度修正
	fR += fCor;
	
	fR2 = GetFloatRound(fR, 1);//四舍五入
	fCor = GetSurfCor(fR2,ht_mian0);//测试面修正
	fR += fCor;
	ftoa(fR,1,str0);
	
	if(fR-MAX_RM>0.00001F)//如果大于60.0，表示推定值大于60.1
	{
		fVal=60.1f;//MORE_THEN_SIXTY;
		return fVal;
	}
	if(fR<MIN_RM-0.0001F) //如果小余10.0，表示小余9.9
	{
		fVal=9.9f;//LESS_THEN_TEN;
		return fVal;
	}
	if(Pumpflag==1)//表示非泵送
	{
		fC=Cardepth;
		//根据平均碳化深度值求出列数
		nC = 0;
		for(i=1; i<=12; i++)
		{
			if(fC+0.25 >= i*0.5F)
				nC ++;
			else
				break;
		}
		//根据修正后的回弹值求出行数

		nR = 0;
		for (i = 1; i < CURVELINE - 1; i++)
		{
			if(fR >= MIN_RM + (float)i*0.2F)
				nR ++;
			else
				break;
		}
		fR1 = MIN_RM+(float)nR*0.2F;
		//查强度换算表，获取换算强度值
		if((Data[nR][nC] < 0.00000001F) | ((Data[nR+1][nC]<0.00000001F)))//强度换算表中的空值
		{
			if(fR<24.0f)
				fVal=9.9f;//LESS_THEN_TEN;
			else
				fVal=60.1f;//MORE_THEN_SIXTY;
		}
		else
		{
			fVal = (float)(Data[nR][nC]+((fR-fR1)/0.2F)*(Data[nR+1][nC]-(Data[nR][nC])));
		}
		fVal=GetFloatRound(fVal,1);	
	}
	else//泵送
	{
		if(fR-52.8>0.00001F)
		{
			fVal=60.1f;//MORE_THEN_SIXTY;
			return fVal;
		}
		if(fR<18.6-0.0001F) 
		{
			fVal=9.9f;//LESS_THEN_TEN;
			return fVal;
		}
		fC=Cardepth;
		//根据平均碳化深度值求出列数
		nC = 0;
		for(i=1; i<=12; i++)
		{
			if(fC+0.25 >= i*0.5F)
				nC ++;
			else
				break;
		}
		//根据修正后的回弹值求出行数
		nR = 0;
		for(i=1; i<CURVELINE-1; i++)
		{
			if(fR > 18.6+i*0.2F)
				nR ++;
			else
				break;
		}
		fR1 = 18.6+nR*0.2F;
		//查强度换算表，获取换算强度值
		if((bengsong[nR][nC] < 0.00000001F) | (bengsong[nR+1][nC]<0.00000001F))//强度换算表中的空值
		{
			if(fR<21.0f)
				fVal=9.9f;//LESS_THEN_TEN;
			else
				fVal=60.1f;//MORE_THEN_SIXTY;
		}
		else
		{
			fVal = (float)(bengsong[nR][nC]+((fR-fR1)/0.2F)*(bengsong[nR+1][nC]-bengsong[nR][nC]));
		}
		fVal=GetFloatRound(fVal, 1);			
	}
	
	return fVal;
}
