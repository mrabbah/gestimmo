
<%@ page contentType="text/html;charset=UTF-8" %>

<html class="${bienEnLigneWindow}">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Entrez vos informations</title>
    <style type="text/css">
      html * {
        margin: 0;
        /*padding: 0; SELECT NOT DISPLAYED CORRECTLY IN FIREFOX */
      }

      /* GENERAL */

      .spinner {
        padding: 5px;
        position: absolute;
        right: 0;
      }

      body {
        background: #fff;
        color: #333;
        font: 11px verdana, arial, helvetica, sans-serif;
      }
      #grailsLogo {
        padding:20px;
      }

      a:link, a:visited, a:hover {
        color: #666;
        font-weight: bold;
        text-decoration: none;
      }

      h1 {
        color: #48802c;
        font-weight: normal;
        font-size: 16px;
        margin: .8em 0 .3em 0;
      }

      ul {
        padding-left: 15px;
      }

      input, select, textarea {
        background-color: #fcfcfc;
        border: 1px solid #ccc;
        font: 11px verdana, arial, helvetica, sans-serif;
        margin: 2px 0;
        padding: 2px 4px;
      }
      select {
        padding: 2px 2px 2px 0;
      }
      textarea {
        width: 250px;
        height: 150px;
        vertical-align: top;
      }

      input:focus, select:focus, textarea:focus {
        border: 1px solid #b2d1ff;
      }

      .body {
        float: left;
        margin: 0 15px 10px 15px;
      }

      /* NAVIGATION MENU */

      .nav {
        background: #fff url(../images/skin/shadow.jpg) bottom repeat-x;
        border: 1px solid #ccc;
        border-style: solid none solid none;
        margin-top: 5px;
        padding: 7px 12px;
      }

      .menuButton {
        font-size: 10px;
        padding: 0 5px;
      }
      .menuButton a {
        color: #333;
        padding: 4px 6px;
      }
      .menuButton a.home {
        background: url(../images/skin/house.png) center left no-repeat;
        color: #333;
        padding-left: 25px;
      }
      .menuButton a.list {
        background: url(../images/skin/database_table.png) center left no-repeat;
        color: #333;
        padding-left: 25px;
      }
      .menuButton a.create {
        background: url(../images/skin/database_add.png) center left no-repeat;
        color: #333;
        padding-left: 25px;
      }

      /* MESSAGES AND ERRORS */

      .message {
        background: #f3f8fc url(../images/skin/information.png) 8px 50% no-repeat;
        border: 1px solid #b2d1ff;
        color: #006dba;
        margin: 10px 0 5px 0;
        padding: 5px 5px 5px 30px
      }

      div.errors {
        background: #fff3f3;
        border: 1px solid red;
        color: #cc0000;
        margin: 10px 0 5px 0;
        padding: 5px 0 5px 0;
      }
      div.errors ul {
        list-style: none;
        padding: 0;
      }
      div.errors li {
        background: url(../images/skin/exclamation.png) 8px 0% no-repeat;
        line-height: 16px;
        padding-left: 30px;
      }

      td.errors select {
        border: 1px solid red;
      }
      td.errors input {
        border: 1px solid red;
      }
      td.errors textarea {
        border: 1px solid red;
      }

      /* TABLES */

      table {
        border: 1px solid #ccc;
        width: 100%
      }
      tr {
        border: 0;
      }
      td, th {
        font: 11px verdana, arial, helvetica, sans-serif;
        line-height: 12px;
        padding: 5px 6px;
        text-align: left;
        vertical-align: top;
      }
      th {
        background: #fff url(../images/skin/shadow.jpg);
        color: #666;
        font-size: 11px;
        font-weight: bold;
        line-height: 17px;
        padding: 2px 6px;
      }
      th a:link, th a:visited, th a:hover {
        color: #333;
        display: block;
        font-size: 10px;
        text-decoration: none;
        width: 100%;
      }
      th.asc a, th.desc a {
        background-position: right;
        background-repeat: no-repeat;
      }
      th.asc a {
        background-image: url(../images/skin/sorted_asc.gif);
      }
      th.desc a {
        background-image: url(../images/skin/sorted_desc.gif);
      }

      .odd {
        background: #f7f7f7;
      }
      .even {
        background: #fff;
      }

      /* LIST */

      .list table {
        border-collapse: collapse;
      }
      .list th, .list td {
        border-left: 1px solid #ddd;
      }
      .list th:hover, .list tr:hover {
        background: #b2d1ff;
      }

      /* PAGINATION */

      .paginateButtons {
        background: #fff url(../images/skin/shadow.jpg) bottom repeat-x;
        border: 1px solid #ccc;
        border-top: 0;
        color: #666;
        font-size: 10px;
        overflow: hidden;
        padding: 10px 3px;
      }
      .paginateButtons a {
        background: #fff;
        border: 1px solid #ccc;
        border-color: #ccc #aaa #aaa #ccc;
        color: #666;
        margin: 0 3px;
        padding: 2px 6px;
      }
      .paginateButtons span {
        padding: 2px 3px;
      }

      /* DIALOG */

      .dialog table {
        padding: 5px 0;
      }

      .prop {
        padding: 5px;
      }
      .prop .name {
        text-align: left;
        width: 15%;
        white-space: nowrap;
      }
      .prop .value {
        text-align: left;
        width: 85%;
      }

      /* ACTION BUTTONS */

      .buttons {
        background: #fff url(../images/skin/shadow.jpg) bottom repeat-x;
        border: 1px solid #ccc;
        color: #666;
        font-size: 10px;
        margin-top: 5px;
        overflow: hidden;
        padding: 0;
      }

      .buttons input {
        background: #fff;
        border: 0;
        color: #333;
        cursor: pointer;
        font-size: 10px;
        font-weight: bold;
        margin-left: 3px;
        overflow: visible;
        padding: 2px 6px;
      }
      .buttons input.delete {
        background: transparent url(../images/skin/database_delete.png) 5px 50% no-repeat;
        padding-left: 28px;
      }
      .buttons input.edit {
        background: transparent url(../images/skin/database_edit.png) 5px 50% no-repeat;
        padding-left: 28px;
      }
      .buttons input.save {
        background: transparent url(../images/skin/database_save.png) 5px 50% no-repeat;
        padding-left: 28px;
      }

    </style>
  </head>
  <body>
    <div class="body">
      <h1><g:message code="default.create.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${clientInstance}">
        <div class="errors">
          <g:renderErrors bean="${clientInstance}" as="list" />
        </div>
      </g:hasErrors>
      <g:form action="save" >
        <div class="dialog">
          <table>
            <tbody>



              <tr class="prop">
                
                <td valign="top" class="name">
                  <label for="cin"><g:message code="client.cin.label" default="Cin" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'cin', 'errors')}">
            <g:textField name="cin" value="${clientInstance?.cin}" />
            <g:textField name="cin" value="${bien?.code}" />
            </td>

            <td valign="top" class="name">
              <label for="nom"><g:message code="client.nom.label" default="Nom" /></label>
            </td>

            <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'nom', 'errors')}">
            <g:textField name="nom" value="${clientInstance?.nom}" />
            </td>

            <td valign="top" class="name">
              <label for="prenom"><g:message code="client.prenom.label" default="Prenom" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'prenom', 'errors')}">
            <g:textField name="prenom" value="${clientInstance?.prenom}" />
            </td>
            </tr>                     

            <tr class="prop">
              <td valign="top" class="name">
                <label for="dateContact"><g:message code="client.dateContact.label" default="Date Contact" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'dateContact', 'errors')}">
            <g:datePicker name="dateContact" precision="day" value="${clientInstance?.dateContact}"  />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="dateNaissance"><g:message code="client.dateNaissance.label" default="Date Naissance" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'dateNaissance', 'errors')}">
            <g:datePicker name="dateNaissance" precision="day" value="${clientInstance?.dateNaissance}" default="none" noSelection="['': '']" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="fixe"><g:message code="client.fixe.label" default="Fixe" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'tel 2', 'errors')}">
            <g:textField name="fixe" value="${clientInstance?.fixe}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="gsm"><g:message code="client.gsm.label" default="Gsm" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'gsm', 'errors')}">
            <g:textField name="gsm" value="${clientInstance?.gsm}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="mail"><g:message code="client.mail.label" default="Mail" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'mail', 'errors')}">
            <g:textField name="mail" value="${clientInstance?.mail}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="sexe"><g:message code="client.sexe.label" default="Sexe" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'sexe', 'errors')}">
            <g:select name="sexe" from="${clientInstance.constraints.sexe.inList}" value="${clientInstance?.sexe}" valueMessagePrefix="client.sexe"  />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="nationalite"><g:message code="client.nationalite.label" default="Nationalite" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'nationalite', 'errors')}">
            <g:select name="nationalite.id" from="${com.choranet.omnidior.gesticom.Nationalite.list()}" optionKey="id" value="${clientInstance?.nationalite?.id}"  />
            </td>
            </tr>


            <tr class="prop">
              <td valign="top" class="name">
                <label for="ville"><g:message code="client.ville.label" default="Ville" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'ville', 'errors')}">
            <g:select name="ville.id" from="${com.choranet.omnidior.gesticom.Ville.list()}" optionKey="id" value="${clientInstance?.ville?.id}"  />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="adresse"><g:message code="client.adresse.label" default="Adresse" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'adresse', 'errors')}">
            <g:textField name="adresse" value="${clientInstance?.adresse}" />
            </td>
            </tr>

            </tbody>
          </table>
        </div>
        <div class="buttons">
          <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Envoyer la demande')}" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
