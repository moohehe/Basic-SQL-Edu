<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<!-- SQL 정답 입력 화면 DIV -->
	
	<div class="editor">
		<div class='success'>
			<div class=''>SUCCESS!</div>
			<button class='btn' onclick='javascript:successStage();'>Next</button>
		</div>
		<div class='fail'>
			<div class=''>FAIL!</div>
			<div class="errorMessage"></div>
		</div>
		
        <div class="editor-pane">
          <div class="input-header">
            <div class="table-name"><div class="btn submitbtn" id="sqltest" onclick="javascript:sqlrun();" >Submit</div></div> SQL Editor
				
          </div>
          <div class="file-window css-view">
            <div class="line-numbers">
              1<br>2<br>3<br>4<br>5<br>6<br>7<br>8<br>9<br>10
            </div>
              <textarea class="input-strobe stripe" id ="sql" type="text" placeholder="Type in a SQL query" style="resize: none;" onkeyup="javascript:text_keyup();" onkeypress="javascript:text_keypress();"></textarea>
              
          </div>
        </div>

        <div class="editor-pane html-view">
          <div class="input-header">
          <div class="table-name" id="table_name">[table_name]</div>
            Table Data	
 	          </div>
          <div class="table-window">
            <div class="markup" id="table_data">
	            <!-- <table class="table_data">
	            	<tr class="col" columnsimg="1"><th>col1</th><th>col2</th><th>col3</th></tr>
	            	<tr class="col" columnsimg="2"><td>a</td><td>b</td><td>c</td></tr>
	            	<tr class="col" columnsimg="3"><td>d</td><td>e</td><td>f</td></tr>
	            	<tr class="col" columnsimg="4"><td>g</td><td>h</td><td>j</td></tr>
	            	<tr class="col" columnsimg="5"><td>i</td><td>k</td><td>l</td></tr>
	           	</table> --> 
	           	<table class="table table-hover">
                                <!-- Table head -->
                                <thead class="green lighten-4">
                                    <tr>
                                        <th> </th>
                                        <th> </th>
                                        <th> </th>
                                        <th> </th>
                                        <th> </th>
                                    </tr>
                                </thead>
                                <!-- Table head -->

                                <!-- Table body -->
                                <tbody>
                                    <tr>
                                        <th scope="row"></th>
                                        <td th_code="1"></td>
                                        <td th_code="1"></td>
                                        <td th_code="1"></td>
                                        <td th_code="1"></td>
                                    </tr>
                                    <tr>
                                        <th scope="row"></th>
                                        <td th_code="2"></td>
                                        <td th_code="2"></td>
                                        <td th_code="2"></td>
                                        <td th_code="2"></td>
                                    </tr>
                                    <tr>
                                        <th scope="row"></th>
                                        <td th_code="3"></td>
                                        <td th_code="3"></td>
                                        <td th_code="3"></td>
                                        <td th_code="3"></td>
                                    </tr>
                                    <tr>
                                        <th scope="row"></th>
                                        <td th_code="4"></td>
                                        <td th_code="4"></td>
                                        <td th_code="4"></td>
                                        <td th_code="4"></td>
                                    </tr>
                                </tbody>
                                <!-- Table body -->
                                <!-- <tbody> 
                                    <tr>
                                        <th scope="row" class="cols" th_code="1">1</th>
                                        <td class="cols" th_code="1">Cell 1</td>
                                        <td class="cols" th_code="1">Cell 2</td>
                                        <td class="cols" th_code="1">Cell 3</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="col" th_code="2">2</th>
                                        <td class="cols" th_code="2">Cell 4</td>
                                        <td class="cols" th_code="2">Cell 5</td>
                                        <td class="cols" th_code="2">Cell 6</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" class="cols" th_code="3">3</th>
                                        <td class="cols" th_code="3">Cell 7</td>
                                        <td class="cols" th_code="3">Cell 8</td>
                                        <td class="cols" th_code="3">Cell 9</td>
                                    </tr>
                                </tbody> -->
                            </table>
           	</div>
          </div>
        </div>
      </div> 
      
	<!-- SQL 정답 입력 화면 DIV 종료 -->
      
      
      
      
      
				<!-- <textarea cols="149" rows="5" id="resultView"></textarea>  -->
				