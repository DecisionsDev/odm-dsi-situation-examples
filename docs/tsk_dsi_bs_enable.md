# 2.1 How to enable business situations in Insight Designer

1.   Open the eclipse.ini file in the Decision Server Insights V8.10.0 installation directory. 
2.   Make a copy of the file to keep a copy of the original file. 
3.   Open the file in a text editor. 
4.   If a `-vmargs` tag is present, add `-Dcom.ibm.ia.situationEnabled=true` otherwise, at the end of the file, add `-vmargs-Dcom.ibm.ia.situationEnabled=true`. 
5.   Save the eclipse.ini file. 
6.   Start Insight Designer. 
7.   In GitHub, download a ZIP file of the entire odm-dsi-situation-examples repository to your computer, and then import the existing projects that you want to view into your Eclipse workspace. 

 [![Next icon](../images/forward_32.png) **Next**](../docs/tsk_dsi_bs_define.md) 

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md) 

