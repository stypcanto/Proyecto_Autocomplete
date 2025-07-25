<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Buscador Inteligente</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<div class="search-container">
    <h3>Buscador de Nombres</h3>

    <input type="text" id="search" class="form-control" placeholder="Escribe un nombre..." autocomplete="off" />

    <ul id="suggestions" class="list-group mt-2"></ul>
</div>

<!-- Script de autocompletado -->
<script src="js/autocomplete.js"></script>

</body>
</html>