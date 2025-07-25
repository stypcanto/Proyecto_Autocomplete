$(document).ready(function () {
    const $search = $('#search');
    const $suggestions = $('#suggestions');

    $search.on('keyup', function () {
        const query = $(this).val().trim();

        if (query.length >= 2) {
            $.ajax({
                url: 'autocomplete', // Servlet mapeado a /autocomplete
                method: 'GET',
                dataType: 'json',
                data: { term: query },
                success: function (data) {
                    $suggestions.empty();

                    if (data.length === 0) {
                        $suggestions.append('<li class="list-group-item text-muted">Sin resultados</li>');
                    } else {
                        $.each(data, function (i, item) {
                            $suggestions.append(`<li class="list-group-item suggestion-item" style="cursor:pointer;">${item}</li>`);
                        });
                    }
                },
                error: function () {
                    $suggestions.empty().append('<li class="list-group-item text-danger">Error de conexión</li>');
                }
            });
        } else {
            $suggestions.empty();
        }
    });

    // Click en sugerencia
    $(document).on('click', '.suggestion-item', function () {
        $search.val($(this).text());
        $suggestions.empty();
    });
});
