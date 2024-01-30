#curl -X POST -H "Content-Type: application/json" --data '
#{
#  "query": "query AllQuery($page: Int!) { all(page: $page) { result } }",
#  "variables": {
#    "page": 2
#  }
#}
#' http://0.0.0.0:8989/api/graphql

curl -X POST -H "Content-Type: application/json" --data '
{
  "query": "query SearchQuery($term: String!, $page: Int!) { search(term: $term, page: $page) { result } }",
  "variables": {
    "term": "IhrSuchbegriff",
    "page": 1
  }
}
' http://0.0.0.0:8989/api/graphql

