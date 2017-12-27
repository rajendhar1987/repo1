<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    <%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PubSub Utility</title>
<style>
html {
	font-family: "Lato", sans-serif;
}


button {
	cursor: pointer;
	border: none;
	outline:none;
	font-size:125%;
	padding: 12px 24px;
	background-color: #f2f2f2;
}


table {
    border-collapse: collapse;
    width: 100%;
}

th, td {
    text-align: left;
    padding: 8px;
}

tr:nth-child(even){background-color: #f2f2f2}

th {
    background-color: #d8d8d8;
    border: 1px solid black
}

textarea {
  font-family: inherit;
}

.title {
	padding: 8px;
	background-color: #00aaff;
    font-size:150%;
    font-weight:bold;
}

.tab {
	display: none;
    -webkit-animation: fadeEffect 0.5s;
    -moz-animation: fadeEffect 0.5s;
    -o-animation: fadeEffect 0.5s;
    animation: fadeEffect 0.5s;
}

@-webkit-keyframes fadeEffect {
    from {opacity: 0;}
    to {opacity: 1;}
}
@-moz-keyframes fadeEffect {
    from {opacity: 0;}
    to {opacity: 1;}
}
@-o-keyframes fadeEffect {
    from {opacity: 0;}
    to {opacity: 1;}
}
@keyframes fadeEffect {
    from {opacity: 0;}
    to {opacity: 1;}
}
</style>
</head>
<body>
<div class="title">PubSub Utility</div><br>
<div class="tabs">
	<button id="editb" onclick="switchTab('edit')">Edit</button>
	<button id="newb" onclick="switchTab('new')">New Topic</button>
	<button id="emailb" onclick="switchTab('email')">New Queue</button>
</div>
<div id="edit" class="tab">
	<div class="title">Edit Topics</div>
	<form method="post" action="dbedit">
		<table>
			<thead><tr>
				<th>TOPIC NAME</th>
				<th>QUEUE NAME</th>
				<th>SUBSCRIBE</th>
				<th>DELETE TOPIC</th>
			</tr></thead>
			<tbody>
				<c:forEach items="${dbquery}" var="businessdata">    
					<tr>
						<td>${businessdata.getBusiness_service()}</td>
						<td>${businessdata.getQueue()}</td>
						<td>
							<c:choose>
								<c:when test="${businessdata.getOrder() == 1}">
									<input type="checkbox" name="${businessdata.getPrimaryKey()}" checked="checked" value="checked"/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="${businessdata.getPrimaryKey()}" value="unchecked"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td><input type="checkbox" name='${businessdata.getDeleteKey()}'></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="submit" value="Submit Changes">
	</form>
</div>

<div id="new" class="tab">
	<div class="title">New Topic</div>
	<form method="post" action="dbnew">
		<table>
			<tr>
				<td width="25%" style="padding-right:1px">Topic name:<span style="float:right;font-family:monospace;">/Netstar/</span></td>
				<!--<td style="padding-left:1px"><input type="text" id="newtopic" name="newtopic" onkeyup="validateTopicName()" style="font-family:monospace"><span id="error" style="display:none;color:red;"> Cannot use underscores, spaces, or quotes.</span></td>-->
				<td style="padding-left:1px"><input type="text" id="newtopic" name="newtopic" onkeyup="validateTopicName()" style="font-family:monospace"><span> (Cannot use underscores, spaces, or quotes.)</span></td>
			</tr>
			<tr>
				<td width="25%">Queue:</td>
				<td><select id="newqueue" name="newqueue">
					
					<c:forEach items="${queues}" var="queue">
						<option value="${queue}">${queue}</option>
					</c:forEach>
					
				</select></td>
			</tr>
			<tr>
				<td width="25%">Subscribe</td>
				<td><input id="subscribe" name="newsubscribe" type="checkbox"></td>
			</tr>
		</table>
		<input type="submit" id="newsubmit" value="Create Topic" onclick="validateTopicName()">
	</form>
</div>

<div id="email" class="tab">
	<div class="title">Request for New Queue</div>
	<form method="post" action="dbemail">
		<table>
			<tr>
				<td>Sender:</td>
				<td><input type="text" name="email_sender"></td>
			</tr>
			<tr>
				<td>Receiver:</td>
				<td><input type="text" name="email_receiver"></td>
			</tr>
			<tr>
				<td>Topics interested:</td>
				<td><textarea rows="6" cols="100" name="email_topics"></textarea></td>
			</tr>
		</table>
		<input type="submit" value="Send Request"/>
	</form>
</div>

<script>
function switchTab(tabName) {
	var t, tabs;
	tabs = document.getElementsByClassName("tab");
	for (t=0; t<tabs.length; t++) {
		tabs[t].style.display = "none";
		document.getElementById(tabs[t].id+"b").style.backgroundColor = "#d8d8d8";
	}
	document.getElementById(tabName).style.display = "block";
	document.getElementById(tabName+"b").style.backgroundColor = "#00aaff";
}
function validateTopicName() {
	var box = document.getElementById("newtopic");
	if (box.value.indexOf("_")>=0 || box.value.indexOf(" ")>=0 || box.value.indexOf("'")>=0 || box.value.indexOf("\"")>=0) {
		//document.getElementById("error").style.display = "inline";
		document.getElementById("newsubmit").disabled = true;
	}
	else {
		//document.getElementById("error").style.display = "none";
		if (box.value.length<=0) document.getElementById("newsubmit").disabled = true;
		else document.getElementById("newsubmit").disabled = false;
	}
}
document.getElementById("subscribe").checked = true;
document.getElementById("newsubmit").disabled = true;
switchTab("edit");
</script>

</body>
</html>