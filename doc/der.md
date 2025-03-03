@startuml

skinparam linetype ortho
skinparam roundcorner 20
skinparam shadowing false
skinparam table {
    BackgroundColor #FFFFFF
    BorderColor #000000
}

entity "movie" as Movie {
    * id: BIGINT <<PK>>
    --
    title: VARCHAR(255) <<NOT NULL>>
    release_year: INT
    is_tv_series: BOOLEAN
}

entity "starship" as Starship {
    * id: BIGINT <<PK>>
    --
    name: VARCHAR(255) <<NOT NULL>>
    movie_id: BIGINT <<FK>>
}

Movie ||..o{ Starship : "hasMany"

@enduml