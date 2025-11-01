package com.example.assignment2.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface MovieRepository {
    // [변경 전] fun searchByTitle(query: String): List<Movie>
    // [변경 후] suspend fun searchByTitle(query: String): List<Movie>
    // 검색은 IO 작업일 수 있으므로 비동기 처리를 위해 suspend 키워드를 붙여주는 것이 좋습니다.
    suspend fun searchByTitle(query: String): List<Movie>

    // [추가] 1. ID로 영화 정보를 찾는 함수의 명세를 인터페이스에 추가합니다.
    fun getMovieById(id: Long): Movie?
}

class MovieRepositoryImpl : MovieRepository {
    // JSON 파싱을 위한 설정
    private val json = Json { ignoreUnknownKeys = true }

    // 앱이 시작될 때 JSON 문자열을 파싱하여 메모리에 영화 리스트를 저장합니다.
    private val movieRawData = """[
        {
  "adult": false,
  "backdrop_path": "/9DYFYhmbXRGsMhfUgXM3VSP9uLX.jpg",
  "genre_ids": [
    27
  ],
  "id": 1038392,
  "original_language": "en",
  "original_title": "The Conjuring: Last Rites",
  "overview": "Paranormal investigators Ed and Lorraine Warren take on one last terrifying case involving mysterious entities they must confront.",
  "popularity": 650.5492,
  "poster_path": "/gXMnx7C3cufzBHPZynWZLUHOMOT.jpg",
  "release_date": "2025-09-03",
  "title": "The Conjuring: Last Rites",
  "video": false,
  "vote_average": 6.937,
  "vote_count": 817
},
{
  "adult": false,
  "backdrop_path": "/ax2qCKU6tUhdkStiCnrDdXKA5xC.jpg",
  "genre_ids": [
    28,
    12
  ],
  "id": 1086910,
  "original_language": "en",
  "original_title": "The Lost Princess",
  "overview": "After an Ayahuasca vision transports him to a haunted castle, Alec meets Hanna who tales him the story of her mother's forced marriage and how the forbidden passion with her father was in danger when the princess pregnancy became obvious",
  "popularity": 398.9663,
  "poster_path": "/31S2ISsDtbnxb0kuXZl1SxSMD0K.jpg",
  "release_date": "2025-10-16",
  "title": "The Lost Princess",
  "video": false,
  "vote_average": 5.556,
  "vote_count": 9
},
{
  "adult": false,
  "backdrop_path": "/dnzq8322gN4QWm8iL5l5jeqc4Nu.jpg",
  "genre_ids": [
    28,
    35,
    878
  ],
  "id": 338969,
  "original_language": "en",
  "original_title": "The Toxic Avenger Unrated",
  "overview": "When a downtrodden janitor, Winston Gooze (Peter Dinklage), is exposed to a catastrophic toxic accident, he’s transformed into a new kind of hero: The Toxic Avenger. Now, Toxie must rise from outcast to savior, taking on ruthless corporate overlords and corrupt forces who threaten his son, his friends, and his community.",
  "popularity": 384.887,
  "poster_path": "/sIonGSpGNtH72OzbJllPOEMNjVU.jpg",
  "release_date": "2025-08-28",
  "title": "The Toxic Avenger Unrated",
  "video": false,
  "vote_average": 6.16,
  "vote_count": 103
},
{
  "adult": false,
  "backdrop_path": "/1RgPyOhN4DRs225BGTlHJqCudII.jpg",
  "genre_ids": [
    16,
    28,
    14,
    53
  ],
  "id": 1311031,
  "original_language": "ja",
  "original_title": "劇場版「鬼滅の刃」無限城編 第一章 猗窩座再来",
  "overview": "The Demon Slayer Corps are drawn into the Infinity Castle, where Tanjiro, Nezuko, and the Hashira face terrifying Upper Rank demons in a desperate fight as the final battle against Muzan Kibutsuji begins.",
  "popularity": 328.3723,
  "poster_path": "/sUsVimPdA1l162FvdBIlmKBlWHx.jpg",
  "release_date": "2025-07-18",
  "title": "Demon Slayer: Kimetsu no Yaiba Infinity Castle",
  "video": false,
  "vote_average": 7.807,
  "vote_count": 443
},
{
  "adult": false,
  "backdrop_path": "/5A01YSCPYoCOZOhh9tU7F3Htxkf.jpg",
  "genre_ids": [
    28,
    14,
    27,
    53
  ],
  "id": 793387,
  "original_language": "ko",
  "original_title": "거룩한 밤: 데몬 헌터스",
  "overview": "When a devil-worshipping criminal network plunges Seoul into chaos, the police turn to Holy Night—a trio of supernatural demon hunters—to restore order and defeat the rising evil.",
  "popularity": 293.9646,
  "poster_path": "/v3Mo77Qjp6pctpD4eJaNT6kFRSB.jpg",
  "release_date": "2025-04-30",
  "title": "Holy Night: Demon Hunters",
  "video": false,
  "vote_average": 6.522,
  "vote_count": 45
},
{
  "adult": false,
  "backdrop_path": "/iZLqwEwUViJdSkGVjePGhxYzbDb.jpg",
  "genre_ids": [
    878,
    53
  ],
  "id": 755898,
  "original_language": "en",
  "original_title": "War of the Worlds",
  "overview": "Will Radford is a top analyst for Homeland Security who tracks potential threats through a mass surveillance program, until one day an attack by an unknown entity leads him to question whether the government is hiding something from him... and from the rest of the world.",
  "popularity": 336.2977,
  "poster_path": "/yvirUYrva23IudARHn3mMGVxWqM.jpg",
  "release_date": "2025-07-29",
  "title": "War of the Worlds",
  "video": false,
  "vote_average": 4.4,
  "vote_count": 617
},
{
  "adult": false,
  "backdrop_path": "/a6RkQIOZ6wThQOEDv6lHsfH53hD.jpg",
  "genre_ids": [
    28,
    80,
    53
  ],
  "id": 1186350,
  "original_language": "ml",
  "original_title": "മാർക്കോ",
  "overview": "The adoptive son of the Adattu family, Marco, sets off on a ruthless quest for vengeance after his brother is brutally murdered, finding only betrayal, loss and unimaginable brutality at every step.",
  "popularity": 276.1862,
  "poster_path": "/il3ao5gcF6fZNqo1o9o7lusmEyU.jpg",
  "release_date": "2024-12-20",
  "title": "Marco",
  "video": false,
  "vote_average": 6.383,
  "vote_count": 60
},
{
  "adult": false,
  "backdrop_path": "/k6tdiMTO39RQj3dhfspuzprfoe0.jpg",
  "genre_ids": [
    80
  ],
  "id": 941109,
  "original_language": "en",
  "original_title": "Play Dirty",
  "overview": "Expert thief Parker gets a shot at a major heist, but to pull it off he and his team must outsmart a South American dictator, the New York mob, and the world's richest man.",
  "popularity": 248.2164,
  "poster_path": "/ovZ0zq0NwRghtWI1oLaM0lWuoEw.jpg",
  "release_date": "2025-09-30",
  "title": "Play Dirty",
  "video": false,
  "vote_average": 6.7,
  "vote_count": 249
},
{
  "adult": false,
  "backdrop_path": "/7FnmZCAjAwaHydgpjFKnu11MoWs.jpg",
  "genre_ids": [
    878,
    12
  ],
  "id": 617126,
  "original_language": "en",
  "original_title": "The Fantastic 4: First Steps",
  "overview": "Against the vibrant backdrop of a 1960s-inspired, retro-futuristic world, Marvel's First Family is forced to balance their roles as heroes with the strength of their family bond, while defending Earth from a ravenous space god called Galactus and his enigmatic Herald, Silver Surfer.",
  "popularity": 230.38,
  "poster_path": "/cm8TNGBGG0aBfWj0LgrESHv8tir.jpg",
  "release_date": "2025-07-23",
  "title": "The Fantastic 4: First Steps",
  "video": false,
  "vote_average": 7.159,
  "vote_count": 1930
},
{
  "adult": false,
  "backdrop_path": "/bWF5ImUscXXYia8owpm8coadR4m.jpg",
  "genre_ids": [
    28,
    27,
    10752
  ],
  "id": 1257009,
  "original_language": "en",
  "original_title": "Primitive War",
  "overview": "During the Vietnam War, a recon unit ventures to an isolated jungle valley to uncover the fate of a missing platoon. They soon find themselves in a fight for their lives against an unexpected enemy — prehistoric dinosaurs.",
  "popularity": 208.9358,
  "poster_path": "/nWBqU5YXmDVJWWEDJ4u3ZSseNVL.jpg",
  "release_date": "2025-08-21",
  "title": "Primitive War",
  "video": false,
  "vote_average": 6.995,
  "vote_count": 95
},
{
  "adult": false,
  "backdrop_path": "/jzzpCJWkVZZwAxzzLmtYN0MBWFd.jpg",
  "genre_ids": [
    27,
    9648
  ],
  "id": 986097,
  "original_language": "en",
  "original_title": "HIM",
  "overview": "After suffering a potentially career-ending brain trauma, Cameron Cade receives a lifeline when his hero, legendary eight-time Championship quarterback and cultural megastar Isaiah White, offers to train Cam at Isaiah's isolated compound that he shares with his celebrity influencer wife. But as Cam's training accelerates, Isaiah's charisma begins to curdle into something darker.",
  "popularity": 190.1058,
  "poster_path": "/qPJzcYR2f1O1uynYBCVPPJuOiAH.jpg",
  "release_date": "2025-09-18",
  "title": "HIM",
  "video": false,
  "vote_average": 5.7,
  "vote_count": 101
},
{
  "adult": false,
  "backdrop_path": "/znMUHotZRTcyIFWFz2bvcpSx6YP.jpg",
  "genre_ids": [
    28
  ],
  "id": 1053008,
  "original_language": "en",
  "original_title": "Fight Another Day",
  "overview": "Follows a tough cop, who after being transported to a dystopian future, must enter a deadly combat tournament to be able to return to his past.",
  "popularity": 156.7085,
  "poster_path": "/fKnLt1xQV0RdpN6RViowEiD1dFW.jpg",
  "release_date": "2024-10-09",
  "title": "Fight Another Day",
  "video": false,
  "vote_average": 5.1,
  "vote_count": 16
},
{
  "adult": false,
  "backdrop_path": "/yOFqBpJ0PEkBdQqalDEaeOiaKbz.jpg",
  "genre_ids": [
    28,
    80,
    53
  ],
  "id": 1267319,
  "original_language": "ko",
  "original_title": "사마귀",
  "overview": "Mantis, an ace assassin, returns to the contract killer industry after a hiatus, encountering his trainee friend Jae-yi and a retired legendary killer Dok-go, who now runs the organization.",
  "popularity": 148.881,
  "poster_path": "/xS9bbciDC5lHPgl79SrPyzxKAXL.jpg",
  "release_date": "2025-09-26",
  "title": "Mantis",
  "video": false,
  "vote_average": 5.97,
  "vote_count": 84
},
{
  "adult": false,
  "backdrop_path": "/w3Bi0wygeFQctn6AqFTwhGNXRwL.jpg",
  "genre_ids": [
    10751,
    14,
    10402,
    35,
    12,
    16
  ],
  "id": 803796,
  "original_language": "en",
  "original_title": "KPop Demon Hunters",
  "overview": "When K-pop superstars Rumi, Mira and Zoey aren't selling out stadiums, they're using their secret powers to protect their fans from supernatural threats.",
  "popularity": 155.5835,
  "poster_path": "/22AouvwlhlXbe3nrFcjzL24bvWH.jpg",
  "release_date": "2025-06-20",
  "title": "KPop Demon Hunters",
  "video": false,
  "vote_average": 8.271,
  "vote_count": 1632
},
{
  "adult": false,
  "backdrop_path": "/a9SLDj7GPKxeuOjoYkgr5nz1IOl.jpg",
  "genre_ids": [
    9648,
    18,
    53
  ],
  "id": 1290879,
  "original_language": "en",
  "original_title": "The Woman in Cabin 10",
  "overview": "On a lavish yacht for an assignment, a journalist sees a passenger go overboard. But when no one believes her, she risks her life to uncover the truth.",
  "popularity": 146.6466,
  "poster_path": "/4kpdyePOlcELQQJcxXPF5GB1Adw.jpg",
  "release_date": "2025-10-09",
  "title": "The Woman in Cabin 10",
  "video": false,
  "vote_average": 6.128,
  "vote_count": 94
},
{
  "adult": false,
  "backdrop_path": "/yGByIyqgJerCw7AAphTrTAlrdkJ.jpg",
  "genre_ids": [
    28,
    10752,
    53,
    36
  ],
  "id": 1328803,
  "original_language": "en",
  "original_title": "Prisoner of War",
  "overview": "British RAF Wing Commander James Wright is captured by the Japanese during WWII and forced to fight in brutal hand-to-hand combat. The Japanese soldiers get more than they bargained for when Wright’s years of martial arts training in Hong Kong prove him to be a formidable opponent.",
  "popularity": 149.5257,
  "poster_path": "/1XET89sjRm9mUuHXhGIlKTNd5uD.jpg",
  "release_date": "2025-09-19",
  "title": "Prisoner of War",
  "video": false,
  "vote_average": 7.12,
  "vote_count": 75
},
{
  "adult": false,
  "backdrop_path": "/pUNfHmVqfwRdILhCkU8TdysVOXo.jpg",
  "genre_ids": [
    878,
    12,
    28
  ],
  "id": 533533,
  "original_language": "en",
  "original_title": "TRON: Ares",
  "overview": "A highly sophisticated Program called Ares is sent from the digital world into the real world on a dangerous mission, marking humankind's first encounter with A.I. beings.",
  "popularity": 153.726,
  "poster_path": "/chpWmskl3aKm1aTZqUHRCtviwPy.jpg",
  "release_date": "2025-10-08",
  "title": "TRON: Ares",
  "video": false,
  "vote_average": 6.678,
  "vote_count": 115
},
{
  "adult": false,
  "backdrop_path": "/zpEWFNqoN8Qg1SzMMHmaGyOBTdW.jpg",
  "genre_ids": [
    28,
    53,
    80
  ],
  "id": 1054867,
  "original_language": "en",
  "original_title": "One Battle After Another",
  "overview": "Washed-up revolutionary Bob exists in a state of stoned paranoia, surviving off-grid with his spirited, self-reliant daughter, Willa. When his evil nemesis resurfaces after 16 years and she goes missing, the former radical scrambles to find her, father and daughter both battling the consequences of his past.",
  "popularity": 127.1627,
  "poster_path": "/m1jFoahEbeQXtx4zArT2FKdbNIj.jpg",
  "release_date": "2025-09-23",
  "title": "One Battle After Another",
  "video": false,
  "vote_average": 7.8,
  "vote_count": 776
},
{
  "adult": false,
  "backdrop_path": "/eGX66zonvc4bXg3rM08RUxdYSDx.jpg",
  "genre_ids": [
    878,
    12,
    28
  ],
  "id": 1061474,
  "original_language": "en",
  "original_title": "Superman",
  "overview": "Superman, a journalist in Metropolis, embarks on a journey to reconcile his Kryptonian heritage with his human upbringing as Clark Kent.",
  "popularity": 132.8143,
  "poster_path": "/wPLysNDLffQLOVebZQCbXJEv6E6.jpg",
  "release_date": "2025-07-09",
  "title": "Superman",
  "video": false,
  "vote_average": 7.452,
  "vote_count": 3561
},
{
  "adult": false,
  "backdrop_path": "/nvdLEfAWcfTNn45SpwBnLGBmOR1.jpg",
  "genre_ids": [
    37
  ],
  "id": 1357886,
  "original_language": "it",
  "original_title": "Django Undisputed",
  "overview": "Determined to exact his justice, Django faces dangerous challenges and bloody clashes to restore order in a territory dominated by lawlessness and injustice.",
  "popularity": 125.1223,
  "poster_path": "/porcUhC4jqFt72TaxbjOQo2lTzL.jpg",
  "release_date": "2024-04-22",
  "title": "Django Undisputed",
  "video": false,
  "vote_average": 6.056,
  "vote_count": 9
},
{
      "adult": false,
      "backdrop_path": "/lL4zajQbvvo63z2CNu36Ue6wpBX.jpg",
      "genre_ids": [
        18,
        9648,
        53
      ],
      "id": 1236470,
      "original_language": "en",
      "original_title": "The Lost Bus",
      "overview": "A determined father risks everything to rescue a dedicated teacher and her students from a raging wildfire.",
      "popularity": 117.3532,
      "poster_path": "/zpygCOYY1DPBkeUsrrznLRN5js5.jpg",
      "release_date": "2025-09-19",
      "title": "The Lost Bus",
      "video": false,
      "vote_average": 7.148,
      "vote_count": 244
    },
    {
      "adult": false,
      "backdrop_path": "/4QXRStvIJHsHLUf6hr7aweMX7em.jpg",
      "genre_ids": [
        28,
        12,
        53,
        80
      ],
      "id": 7451,
      "original_language": "en",
      "original_title": "xXx",
      "overview": "Xander Cage is your standard adrenaline junkie with no fear and a lousy attitude. When the US Government \"recruits\" him to go on a mission, he's not exactly thrilled. His mission: to gather information on an organization that may just be planning the destruction of the world, led by the nihilistic Yorgi.",
      "popularity": 111.692,
      "poster_path": "/xeEw3eLeSFmJgXZzmF2Efww0q3s.jpg",
      "release_date": "2002-08-09",
      "title": "xXx",
      "video": false,
      "vote_average": 5.967,
      "vote_count": 4582
    },
    {
      "adult": false,
      "backdrop_path": "/yo1DleICj1CLlUjXymo5pNMJDFd.jpg",
      "genre_ids": [
        53,
        27
      ],
      "id": 1547527,
      "original_language": "en",
      "original_title": "Stay",
      "overview": "Kiara, a PhD author on African spirituality, and Miles, a former MMA fighter turned trainer, facing the end of their marriage. They encounter supernatural forces and go from fighting one another to fighting for their lives. To survive, they must conquer darkness with love.",
      "popularity": 121.7496,
      "poster_path": "/kRMzvzxjClWJn7bQtaRLQ2qhb6c.jpg",
      "release_date": "2025-10-07",
      "title": "Stay",
      "video": false,
      "vote_average": 2,
      "vote_count": 3
    },
    {
      "adult": false,
      "backdrop_path": "/aIsjCdfiAS89cMjdDWEpKmUTHsZ.jpg",
      "genre_ids": [
        28,
        10752
      ],
      "id": 1009640,
      "original_language": "en",
      "original_title": "Valiant One",
      "overview": "With tensions between North and South Korea, a US helicopter crashes on the North side. The survivors must work together to protect a civilian tech specialist and find their way out without the help of US military support.",
      "popularity": 118.5568,
      "poster_path": "/sT8Z14RDCAd6szzxzWFAU4xcMwg.jpg",
      "release_date": "2025-01-30",
      "title": "Valiant One",
      "video": false,
      "vote_average": 6.419,
      "vote_count": 43
    },
    {
      "adult": false,
      "backdrop_path": "/AeDzjt00Hfh9CuW7TIUdYBJmWYM.jpg",
      "genre_ids": [
        14,
        10751,
        28,
        12
      ],
      "id": 1087192,
      "original_language": "en",
      "original_title": "How to Train Your Dragon",
      "overview": "On the rugged isle of Berk, where Vikings and dragons have been bitter enemies for generations, Hiccup stands apart, defying centuries of tradition when he befriends Toothless, a feared Night Fury dragon. Their unlikely bond reveals the true nature of dragons, challenging the very foundations of Viking society.",
      "popularity": 109.1043,
      "poster_path": "/53dsJ3oEnBhTBVMigWJ9tkA5bzJ.jpg",
      "release_date": "2025-06-06",
      "title": "How to Train Your Dragon",
      "video": false,
      "vote_average": 8,
      "vote_count": 2023
    },
    {
      "adult": false,
      "backdrop_path": "/Q2OajDi2kcO6yErb1IAyVDTKMs.jpg",
      "genre_ids": [
        27,
        9648
      ],
      "id": 1078605,
      "original_language": "en",
      "original_title": "Weapons",
      "overview": "When all but one child from the same class mysteriously vanish on the same night at exactly the same time, a community is left questioning who or what is behind their disappearance.",
      "popularity": 109.14,
      "poster_path": "/cpf7vsRZ0MYRQcnLWteD5jK9ymT.jpg",
      "release_date": "2025-08-04",
      "title": "Weapons",
      "video": false,
      "vote_average": 7.409,
      "vote_count": 1852
    },
    {
      "adult": false,
      "backdrop_path": "/da1mXvCrQrFcw6BxovCavBFbIvz.jpg",
      "genre_ids": [
        27,
        53
      ],
      "id": 1251717,
      "original_language": "en",
      "original_title": "Vicious",
      "overview": "A young woman must spend the night fighting for her existence as she slips down a disturbing rabbit hole contained inside a mysterious gift from a late-night visitor.",
      "popularity": 106.8749,
      "poster_path": "/shKGDGrwqmc9x2aTJcQszoXinq0.jpg",
      "release_date": "2025-09-19",
      "title": "Vicious",
      "video": false,
      "vote_average": 6.019,
      "vote_count": 26
    },
    {
      "adult": false,
      "backdrop_path": "/vW25IT0VyK99drGVdVGpMusohbl.jpg",
      "genre_ids": [
        27,
        14
      ],
      "id": 1385169,
      "original_language": "en",
      "original_title": "Rumpelstiltskin",
      "overview": "Once upon a time, a miller attempted to marry his daughter off to the King by promising she could spin straw into gold. With the help of a demonic imp, she completes the task, but the King becomes greedy and wants more. So, she promises the imp her firstborn in return for yet more favours. But when it comes time to honour her bargain, dark forces merge when the Imp makes a deal with the devil.",
      "popularity": 91.1504,
      "poster_path": "/2irI6Bx5dU754W52rReruUj3RI2.jpg",
      "release_date": "2025-03-08",
      "title": "Rumpelstiltskin",
      "video": false,
      "vote_average": 6.273,
      "vote_count": 11
    },
    {
      "adult": false,
      "backdrop_path": "/gZF6fDtE71Fi3AGcF0PSQAxCgE3.jpg",
      "genre_ids": [
        28
      ],
      "id": 1552819,
      "original_language": "zh",
      "original_title": "Assassin",
      "overview": "",
      "popularity": 98.3717,
      "poster_path": "/fLRCNUz3MsnC0MdhaWpw3hp4Eye.jpg",
      "release_date": "2025-09-26",
      "title": "Assassin",
      "video": false,
      "vote_average": 4.375,
      "vote_count": 8
    },
    {
      "adult": false,
      "backdrop_path": "/538U9snNc2fpnOmYXAPUh3zn31H.jpg",
      "genre_ids": [
        28,
        12,
        53
      ],
      "id": 575265,
      "original_language": "en",
      "original_title": "Mission: Impossible - The Final Reckoning",
      "overview": "Ethan Hunt and team continue their search for the terrifying AI known as the Entity — which has infiltrated intelligence networks all over the globe — with the world's governments and a mysterious ghost from Hunt's past on their trail. Joined by new allies and armed with the means to shut the Entity down for good, Hunt is in a race against time to prevent the world as we know it from changing forever.",
      "popularity": 114.6856,
      "poster_path": "/z53D72EAOxGRqdr7KXXWp9dJiDe.jpg",
      "release_date": "2025-05-17",
      "title": "Mission: Impossible - The Final Reckoning",
      "video": false,
      "vote_average": 7.264,
      "vote_count": 1893
    },
    {
      "adult": false,
      "backdrop_path": "/fncHijpWjitFBmj7SX5z148XEhP.jpg",
      "genre_ids": [
        878,
        12,
        28
      ],
      "id": 1234821,
      "original_language": "en",
      "original_title": "Jurassic World Rebirth",
      "overview": "Five years after the events of Jurassic World Dominion, covert operations expert Zora Bennett is contracted to lead a skilled team on a top-secret mission to secure genetic material from the world's three most massive dinosaurs. When Zora's operation intersects with a civilian family whose boating expedition was capsized, they all find themselves stranded on an island where they come face-to-face with a sinister, shocking discovery that's been hidden from the world for decades.",
      "popularity": 97.7879,
      "poster_path": "/1RICxzeoNCAO5NpcRMIgg1XT6fm.jpg",
      "release_date": "2025-07-01",
      "title": "Jurassic World Rebirth",
      "video": false,
      "vote_average": 6.397,
      "vote_count": 2285
    },
    {
      "adult": false,
      "backdrop_path": "/ZtcGMc204JsNqfjS9lU6udRgpo.jpg",
      "genre_ids": [
        28,
        18
      ],
      "id": 911430,
      "original_language": "en",
      "original_title": "F1",
      "overview": "Racing legend Sonny Hayes is coaxed out of retirement to lead a struggling Formula 1 team—and mentor a young hotshot driver—while chasing one more chance at glory.",
      "popularity": 96.0921,
      "poster_path": "/9PXZIUsSDh4alB80jheWX4fhZmy.jpg",
      "release_date": "2025-06-25",
      "title": "F1",
      "video": false,
      "vote_average": 7.831,
      "vote_count": 2336
    },
    {
      "adult": false,
      "backdrop_path": "/dh0dLVLDLqUKhtytCFjkf3EHeJI.jpg",
      "genre_ids": [
        16,
        28,
        14
      ],
      "id": 1218925,
      "original_language": "ja",
      "original_title": "チェンソーマン レゼ篇",
      "overview": "In a brutal war between devils, hunters, and secret enemies, a mysterious girl named Reze has stepped into Denji's world, and he faces his deadliest battle yet, fueled by love in a world where survival knows no rules.",
      "popularity": 95.1309,
      "poster_path": "/xdzLBZjCVSEsic7m7nJc4jNJZVW.jpg",
      "release_date": "2025-09-19",
      "title": "Chainsaw Man - The Movie: Reze Arc",
      "video": false,
      "vote_average": 8.55,
      "vote_count": 20
    },
    {
      "adult": false,
      "backdrop_path": "/3IcJRIjMo4G1A6HtCHiKCUAyfIw.jpg",
      "genre_ids": [
        27,
        28,
        878
      ],
      "id": 1032448,
      "original_language": "en",
      "original_title": "Jurassic Triangle",
      "overview": "A group of colleagues crash land on a mysterious island that they cannot escape. They discover they are not alone when they encounter an array of blood thirsty creatures and dinosaurs out to get them. Will they survive the island?",
      "popularity": 76.8591,
      "poster_path": "/yLxLz8RHiOLtNvKCtJYQgDFXqBU.jpg",
      "release_date": "2024-02-13",
      "title": "Jurassic Triangle",
      "video": false,
      "vote_average": 2.8,
      "vote_count": 10
    },
    {
      "adult": false,
      "backdrop_path": "/uxRCrEMJlfCILQhkcDuDpadkQC9.jpg",
      "genre_ids": [
        10749,
        35
      ],
      "id": 1352755,
      "original_language": "en",
      "original_title": "Maintenance Required",
      "overview": "Charlie, the fiercely independent owner of an all-female mechanic shop, is forced to reevaluate her future when a flashy corporate competitor moves in across the street. Seeking comfort, she turns to an anonymous online confidant - unaware she's confiding in Beau, the very rival threatening her business. As sparks fly both online and off, the truth threatens to blow everything apart.",
      "popularity": 104.5664,
      "poster_path": "/lM7Q1y0pmHPWZCPmGkAWGKdgu6W.jpg",
      "release_date": "2025-10-07",
      "title": "Maintenance Required",
      "video": false,
      "vote_average": 5.8,
      "vote_count": 36
    },
    {
      "adult": false,
      "backdrop_path": "/mEW9XMgYDO6U0MJcIRqRuSwjzN5.jpg",
      "genre_ids": [
        28,
        53
      ],
      "id": 1007734,
      "original_language": "en",
      "original_title": "Nobody 2",
      "overview": "Former assassin Hutch Mansell takes his family on a nostalgic vacation to a small-town theme park, only to be pulled back into violence when they clash with a corrupt operator, a crooked sheriff, and a ruthless crime boss.",
      "popularity": 84.2777,
      "poster_path": "/xGLoqM9peusKQeuwlSw2Qlhx740.jpg",
      "release_date": "2025-08-13",
      "title": "Nobody 2",
      "video": false,
      "vote_average": 7.118,
      "vote_count": 801
    },
    {
      "adult": false,
      "backdrop_path": "/lB96EMr5A7Og81UhyldJvvy35r.jpg",
      "genre_ids": [
        10751,
        35,
        80,
        12,
        16
      ],
      "id": 1175942,
      "original_language": "en",
      "original_title": "The Bad Guys 2",
      "overview": "The now-reformed Bad Guys are trying (very, very hard) to be good, but instead find themselves hijacked into a high-stakes, globe-trotting heist, masterminded by a new team of criminals they never saw coming: The Bad Girls.",
      "popularity": 84.39,
      "poster_path": "/c1msaKf1wyuKcmLjjJd6rIBPFcd.jpg",
      "release_date": "2025-07-24",
      "title": "The Bad Guys 2",
      "video": false,
      "vote_average": 7.8,
      "vote_count": 458
    },
    {
      "adult": false,
      "backdrop_path": "/c2dPK558peXs9QzacwzHYQqqS2r.jpg",
      "genre_ids": [
        35,
        18
      ],
      "id": 1277988,
      "original_language": "pt",
      "original_title": "Caramelo",
      "overview": "After a life-changing diagnosis, a promising chef finds hope and humor with the help of a fur-midable dog friend.",
      "popularity": 93.1081,
      "poster_path": "/ur3H6g9xmrLQX2Ioh9z3P904ZSC.jpg",
      "release_date": "2025-10-08",
      "title": "Caramelo",
      "video": false,
      "vote_average": 6.419,
      "vote_count": 31
    },
    {
      "adult": false,
      "backdrop_path": "/m5UwDL3nLNQn9UDfteylgFIpxCJ.jpg",
      "genre_ids": [
        18,
        35
      ],
      "id": 1242404,
      "original_language": "en",
      "original_title": "Steve",
      "overview": "Over one intense day, the devoted head teacher of a last-chance reform school strives to keep his students in line while facing pressures of his own.",
      "popularity": 64.3044,
      "poster_path": "/wmLoMyofbseLfxiGgk1Iz5H97c3.jpg",
      "release_date": "2025-09-19",
      "title": "Steve",
      "video": false,
      "vote_average": 6.4,
      "vote_count": 90
    },
    {
      "adult": false,
      "backdrop_path": "/fBlzTwgtbDYkDKlhnPu69jHfVGy.jpg",
      "genre_ids": [
        27,
        10749
      ],
      "id": 1242011,
      "original_language": "en",
      "original_title": "Together",
      "overview": "With a move to the countryside already testing the limits of a couple's relationship, a supernatural encounter begins an extreme transformation of their love, their lives, and their flesh.",
      "popularity": 59.7008,
      "poster_path": "/m52XidzKx94bKlToZfEXUnL3pdy.jpg",
      "release_date": "2025-07-28",
      "title": "Together",
      "video": false,
      "vote_average": 7.206,
      "vote_count": 582
    },


    {
      "adult": false,
      "backdrop_path": "/oz4U9eA6ilYf1tyiVuGmkftdLac.jpg",
      "genre_ids": [
        10749,
        18
      ],
      "id": 1010581,
      "original_language": "es",
      "original_title": "Culpa mía",
      "overview": "Noah must leave her city, boyfriend, and friends to move into William Leister's mansion, the flashy and wealthy husband of her mother Rafaela. As a proud and independent 17 year old, Noah resists living in a mansion surrounded by luxury. However, it is there where she meets Nick, her new stepbrother, and the clash of their strong personalities becomes evident from the very beginning.",
      "popularity": 64.4382,
      "poster_path": "/w46Vw536HwNnEzOa7J24YH9DPRS.jpg",
      "release_date": "2023-06-08",
      "title": "My Fault",
      "video": false,
      "vote_average": 7.774,
      "vote_count": 3819
    },
    {
      "adult": false,
      "backdrop_path": "/boprqXZexBatujVC07gZzzxjWk.jpg",
      "genre_ids": [
        53,
        27
      ],
      "id": 1109406,
      "original_language": "en",
      "original_title": "Compulsion",
      "overview": "Two women—Diana, a flamboyant and ruthless thief, and Evie, a seemingly innocent young woman with a troubled past—become embroiled in a series of horrific murders on the island of Malta.",
      "popularity": 54.8262,
      "poster_path": "/s3MzSdsMcmxmpVJMuH5ezNPjzs7.jpg",
      "release_date": "2024-11-10",
      "title": "Compulsion",
      "video": false,
      "vote_average": 6.5,
      "vote_count": 12
    },
    {
      "adult": false,
      "backdrop_path": "/9KSboWOt09J72aMY4x8SS1IaOHK.jpg",
      "genre_ids": [
        16,
        28,
        12,
        14
      ],
      "id": 987400,
      "original_language": "es",
      "original_title": "Batman Azteca: Choque de imperios",
      "overview": "In the time of the Aztec empire, tragedy strikes Yohualli Coatl when his father is murdered by Spanish conquistadors. To warn King Moctezuma and his high priest, Yoka, of imminent danger, Yohualli escapes to Tenochtitlán. There, he trains in the temple of the bat god Tzinacan with his mentor, developing equipment and weaponry to confront the Spanish invasion and avenge his father’s death. Along the way, he encounters key figures like the fierce Jaguar Woman and the conquistador Hernán Cortés.",
      "popularity": 65.0556,
      "poster_path": "/s0Mlo5w4INp3bUdsJo3RaSSMXWz.jpg",
      "release_date": "2025-09-18",
      "title": "Aztec Batman: Clash of Empires",
      "video": false,
      "vote_average": 7.9,
      "vote_count": 94
    },
    {
      "adult": false,
      "backdrop_path": "/7Zx3wDG5bBtcfk8lcnCWDOLM4Y4.jpg",
      "genre_ids": [
        10751,
        878,
        35,
        12
      ],
      "id": 552524,
      "original_language": "en",
      "original_title": "Lilo & Stitch",
      "overview": "The wildly funny and touching story of a lonely Hawaiian girl and the fugitive alien who helps to mend her broken family.",
      "popularity": 63.6744,
      "poster_path": "/ckQzKpQJO4ZQDCN5evdpKcfm7Ys.jpg",
      "release_date": "2025-05-17",
      "title": "Lilo & Stitch",
      "video": false,
      "vote_average": 7.257,
      "vote_count": 1727
    },
    {
      "adult": false,
      "backdrop_path": "/8btfz81bOJ2lC7cujYBTw03wzg3.jpg",
      "genre_ids": [
        16,
        14,
        12,
        28
      ],
      "id": 980477,
      "original_language": "zh",
      "original_title": "哪吒之魔童闹海",
      "overview": "After a catastrophic event leaves their bodies destroyed, Ne Zha and Ao Bing are granted a fragile second chance at life. As tensions rise between the dragon clans and celestial forces, the two must undergo a series of perilous trials that will test their bond, challenge their identities, and decide the fate of both mortals and immortals.",
      "popularity": 62.0008,
      "poster_path": "/cb5NyNrqiCNNoDkA8FfxHAtypdG.jpg",
      "release_date": "2025-01-29",
      "title": "Ne Zha 2",
      "video": false,
      "vote_average": 8.046,
      "vote_count": 395
    },
    {
      "adult": false,
      "backdrop_path": "/yQy9Y3p5INwkfTuHSnzYnz4MCV3.jpg",
      "genre_ids": [
        35,
        14,
        10751
      ],
      "id": 1125257,
      "original_language": "en",
      "original_title": "Freakier Friday",
      "overview": "Years after Tess and Anna endured an identity crisis, Anna now has a daughter of her own and a soon-to-be stepdaughter. As they navigate the myriad challenges that come when two families merge, Tess and Anna discover lightning might indeed strike twice.",
      "popularity": 58.7709,
      "poster_path": "/9wV65OmsjLAqBfDnYTkMPutXH8j.jpg",
      "release_date": "2025-08-06",
      "title": "Freakier Friday",
      "video": false,
      "vote_average": 7,
      "vote_count": 258
    },
    {
      "adult": false,
      "backdrop_path": "/kzeBfhXMRWiykBsqoL3UbfaM0S.jpg",
      "genre_ids": [
        35,
        28,
        80
      ],
      "id": 1035259,
      "original_language": "en",
      "original_title": "The Naked Gun",
      "overview": "Only one man has the particular set of skills... to lead Police Squad and save the world: Lt. Frank Drebin Jr.",
      "popularity": 58.1476,
      "poster_path": "/rmwQ8GsdQ1M3LtemNWLErle2nBU.jpg",
      "release_date": "2025-07-30",
      "title": "The Naked Gun",
      "video": false,
      "vote_average": 6.597,
      "vote_count": 889
    },
    {
      "adult": false,
      "backdrop_path": "/eQV9rSQ6S1ja4lGTwHTnQuVhoG.jpg",
      "genre_ids": [
        27,
        35,
        14,
        18
      ],
      "id": 1284120,
      "original_language": "no",
      "original_title": "Den stygge stesøsteren",
      "overview": "In a fairy-tale kingdom where beauty is a brutal business, Elvira battles to compete with her incredibly beautiful stepsister, and she will go to any length to catch the prince’s eye.",
      "popularity": 57.1668,
      "poster_path": "/crX9rSg9EohybhkEe8iTQUCe55y.jpg",
      "release_date": "2025-03-07",
      "title": "The Ugly Stepsister",
      "video": false,
      "vote_average": 7.235,
      "vote_count": 465
    },
    {
      "adult": false,
      "backdrop_path": "/5U3VLeWr2H4Nd3G7IkHItFimsIz.jpg",
      "genre_ids": [
        10751,
        35,
        12,
        16,
        878
      ],
      "id": 870360,
      "original_language": "en",
      "original_title": "The Day the Earth Blew Up: A Looney Tunes Movie",
      "overview": "Porky and Daffy, the classic animated odd couple, turn into unlikely heroes when their antics at the local bubble gum factory uncover a secret alien mind control plot. Against all odds, the two are determined to save their town (and the world!)...that is if they don't drive each other crazy in the process.",
      "popularity": 56.8986,
      "poster_path": "/s2lB1kaYCdGSnZX5meQCiOR6HfX.jpg",
      "release_date": "2024-08-01",
      "title": "The Day the Earth Blew Up: A Looney Tunes Movie",
      "video": false,
      "vote_average": 7.679,
      "vote_count": 170
    },
    {
      "adult": false,
      "backdrop_path": "/vNUwK5P42m81uG57kKI1WxSZwIQ.jpg",
      "genre_ids": [
        35,
        10749
      ],
      "id": 1403735,
      "original_language": "te",
      "original_title": "లైలా",
      "overview": "Sonu Model, a renowned beautician from the old city, is forced to disguise himself as Laila, leading to a series of comedic, romantic, and action-packed events. Chaos ensues in this hilarious laugh riot",
      "popularity": 59.9905,
      "poster_path": "/l4gsNxFPGpzbq0D6QK1a8vO1lBz.jpg",
      "release_date": "2025-02-14",
      "title": "Laila",
      "video": false,
      "vote_average": 5.8,
      "vote_count": 5
    },
    {
      "adult": false,
      "backdrop_path": "/2nwhxEyefcIFKwOrSigiamoIzu2.jpg",
      "genre_ids": [
        28,
        53,
        18
      ],
      "id": 1119878,
      "original_language": "en",
      "original_title": "Ice Road: Vengeance",
      "overview": "Big rig ice road driver Mike McCann travels to Nepal to scatter his late brother’s ashes on Mt. Everest. While on a packed tour bus traversing the deadly 12,000 ft. terrain of the infamous Road to the Sky, McCann and his mountain guide encounter a group of mercenaries and must fight to save themselves, the busload of innocent travelers, and the local villagers’ homeland.",
      "popularity": 54.7593,
      "poster_path": "/cQN9rZj06rXMVkk76UF1DfBAico.jpg",
      "release_date": "2025-06-27",
      "title": "Ice Road: Vengeance",
      "video": false,
      "vote_average": 6.313,
      "vote_count": 466
    },
    {
      "adult": false,
      "backdrop_path": "/xR75PgmIxhS6ChjNUKgz4krC9MR.jpg",
      "genre_ids": [
        27,
        9648,
        53
      ],
      "id": 957121,
      "original_language": "en",
      "original_title": "The Man in My Basement",
      "overview": "Down on his luck, Charles Blakey agrees to rent his basement to a mysterious stranger, unaware he may be letting in a force much darker than he imagined.",
      "popularity": 46.6225,
      "poster_path": "/1zpeH8wOrM8p2TTAmBetwhe2BIx.jpg",
      "release_date": "2025-09-12",
      "title": "The Man in My Basement",
      "video": false,
      "vote_average": 5.296,
      "vote_count": 27
    },
    {
      "adult": false,
      "backdrop_path": "/2IIKts2A9vnUdM9tTC76B8tDmuZ.jpg",
      "genre_ids": [
        27
      ],
      "id": 1151031,
      "original_language": "en",
      "original_title": "Bring Her Back",
      "overview": "Following the death of their father, a brother and sister are introduced to their new sibling by their foster mother, only to learn that she has a terrifying secret.",
      "popularity": 48.6621,
      "poster_path": "/1Q3GlCXGYWELifxANYZ5OVMRVZl.jpg",
      "release_date": "2025-05-28",
      "title": "Bring Her Back",
      "video": false,
      "vote_average": 7.41,
      "vote_count": 1015
    },
    {
      "adult": false,
      "backdrop_path": "/9REO1DLpmwhrBJY3mYW5eVxkXFM.jpg",
      "genre_ids": [
        12,
        18,
        878
      ],
      "id": 157336,
      "original_language": "en",
      "original_title": "Interstellar",
      "overview": "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",
      "popularity": 51.6857,
      "poster_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
      "release_date": "2014-11-05",
      "title": "Interstellar",
      "video": false,
      "vote_average": 8.461,
      "vote_count": 38013
    },
    {
      "adult": false,
      "backdrop_path": "/k24eZq5I3jyz4htPkZCRpnUmBzE.jpg",
      "genre_ids": [
        10749,
        18
      ],
      "id": 1156593,
      "original_language": "es",
      "original_title": "Culpa tuya",
      "overview": "The love between Noah and Nick seems unwavering despite their parents' attempts to separate them. But his job and her entry into college open up their lives to new relationships that will shake the foundations of both their relationship and the Leister family itself.",
      "popularity": 52.2414,
      "poster_path": "/1sQA7lfcF9yUyoLYC0e6Zo3jmxE.jpg",
      "release_date": "2024-12-26",
      "title": "Your Fault",
      "video": false,
      "vote_average": 7.124,
      "vote_count": 1373
    },
    {
      "adult": false,
      "backdrop_path": "/1yktYsxkmUtUFTUnCAUaqG6FEiz.jpg",
      "genre_ids": [
        28,
        53,
        80
      ],
      "id": 541671,
      "original_language": "en",
      "original_title": "Ballerina",
      "overview": "Taking place during the events of John Wick: Chapter 3 – Parabellum, Eve Macarro begins her training in the assassin traditions of the Ruska Roma.",
      "popularity": 49.9564,
      "poster_path": "/4sbqReLivBN4e7OOwG6PkSGcKHt.jpg",
      "release_date": "2025-06-04",
      "title": "Ballerina",
      "video": false,
      "vote_average": 7.346,
      "vote_count": 1815
    },
    {
      "adult": false,
      "backdrop_path": "/m2e8GUES92IaXRCLxWElJnlZ34w.jpg",
      "genre_ids": [
        10749,
        18,
        10402
      ],
      "id": 1371189,
      "original_language": "en",
      "original_title": "Ruth & Boaz",
      "overview": "A talented singer leaves the Atlanta music scene and starts over in small-town Tennessee, where she finds love and new purpose but can't escape the past.",
      "popularity": 47.0342,
      "poster_path": "/mtMJEIHjeHcLtvP0tFPAptk1Var.jpg",
      "release_date": "2025-09-25",
      "title": "Ruth & Boaz",
      "video": false,
      "vote_average": 7.635,
      "vote_count": 48
    },
    {
      "adult": false,
      "backdrop_path": null,
      "genre_ids": [
        18
      ],
      "id": 1167828,
      "original_language": "es",
      "original_title": "St. Vierja Academy",
      "overview": "The death of a school friend forces Toño to face the ghosts of his past while at the renowned \"St. Vierja Academy,\" one of Panama's most exclusive catholic schools for boys during the 1970s.",
      "popularity": 47.2294,
      "poster_path": "/qNKFJ8YoOffJedQWAZUVTrz512H.jpg",
      "release_date": "2023-09-21",
      "title": "St. Vierja Academy",
      "video": false,
      "vote_average": 0,
      "vote_count": 0
    },
    {
      "adult": false,
      "backdrop_path": "/7yzpHj2ssuwJERjj4y2MDoZDtRN.jpg",
      "genre_ids": [
        10751,
        35,
        12,
        16,
        878
      ],
      "id": 1022787,
      "original_language": "en",
      "original_title": "Elio",
      "overview": "Elio, a space fanatic with an active imagination, finds himself on a cosmic misadventure where he must form new bonds with eccentric alien lifeforms, navigate a crisis of intergalactic proportions and somehow discover who he is truly meant to be.",
      "popularity": 50.1421,
      "poster_path": "/7z8jDiTZZco9moIKpTUImFtTy7o.jpg",
      "release_date": "2025-06-18",
      "title": "Elio",
      "video": false,
      "vote_average": 6.94,
      "vote_count": 556
    },
    {
      "adult": false,
      "backdrop_path": "/bVQe2nzhh4AvI3yDu22hDK6fTfo.jpg",
      "genre_ids": [
        27,
        35
      ],
      "id": 1396558,
      "original_language": "en",
      "original_title": "Bloody Axe Wound",
      "overview": "Abbie Bladecut is a teenager torn between the macabre traditions of her family’s bloody trade and the tender stirrings of her first crush. In the small town of Clover Falls, Abbie’s father Roger has built an infamous legacy by capturing real-life killings on tape and selling them to eager customers, but as Abbie delves deeper into the grisly family business, she begins to wonder if it’s time to take the family tradition in a new direction.",
      "popularity": 47.6078,
      "poster_path": "/dS9sU5JGnkmEVZ9ubgnJa6RMLwx.jpg",
      "release_date": "2024-12-27",
      "title": "Bloody Axe Wound",
      "video": false,
      "vote_average": 5,
      "vote_count": 22
    },
{
      "adult": false,
      "backdrop_path": "/8rpDcsfLJypbO6vREc0547VKqEv.jpg",
      "genre_ids": [
        28,
        12,
        14,
        878
      ],
      "id": 76600,
      "original_language": "en",
      "original_title": "Avatar: The Way of Water",
      "overview": "Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.",
      "popularity": 43.0428,
      "poster_path": "/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
      "release_date": "2022-12-14",
      "title": "Avatar: The Way of Water",
      "video": false,
      "vote_average": 7.611,
      "vote_count": 12938
    },
    {
      "adult": false,
      "backdrop_path": "/uIpJPDNFoeX0TVml9smPrs9KUVx.jpg",
      "genre_ids": [
        27,
        9648
      ],
      "id": 574475,
      "original_language": "en",
      "original_title": "Final Destination Bloodlines",
      "overview": "Plagued by a violent recurring nightmare, college student Stefanie heads home to track down the one person who might be able to break the cycle and save her family from the grisly demise that inevitably awaits them all.",
      "popularity": 48.2447,
      "poster_path": "/6WxhEvFsauuACfv8HyoVX6mZKFj.jpg",
      "release_date": "2025-05-14",
      "title": "Final Destination Bloodlines",
      "video": false,
      "vote_average": 7.114,
      "vote_count": 2239
    },
    {
      "adult": false,
      "backdrop_path": "/lCpUhIMeklfEvC6BlaxGmSwKXN0.jpg",
      "genre_ids": [
        18,
        10749
      ],
      "id": 1477114,
      "original_language": "tl",
      "original_title": "Sorority",
      "overview": "A tale of seduction and mystery. A fearless pre-law student joins an elite sorority led by a powerful heiress. As secrets unfold, she’s torn between the woman who awakened her desire and a truth that could destroy them both.",
      "popularity": 49.3964,
      "poster_path": "/zibf9SwgiEW7Od5kwdRLSoQygRd.jpg",
      "release_date": "2025-06-20",
      "title": "Sorority",
      "video": false,
      "vote_average": 6.6,
      "vote_count": 5
    },
    {
      "adult": false,
      "backdrop_path": "/8FSB22d8lFe7OIHNXVcw8t8OSri.jpg",
      "genre_ids": [
        53,
        10752,
        80
      ],
      "id": 1255795,
      "original_language": "en",
      "original_title": "Putin",
      "overview": "A portrait of a formidable political figure, unraveling the contrasting facets of an iron-fisted tyrant and a man haunted by fear.",
      "popularity": 39.264,
      "poster_path": "/j3Q2MrpmaOrKfN2pInNvRNhYa4G.jpg",
      "release_date": "2025-01-09",
      "title": "Putin",
      "video": false,
      "vote_average": 5.657,
      "vote_count": 36
    },
    {
      "adult": false,
      "backdrop_path": "/p4OIGFfVo3vbROMD1evVF0u14Rf.jpg",
      "genre_ids": [
        10749,
        35
      ],
      "id": 1289888,
      "original_language": "fr",
      "original_title": "French Lover",
      "overview": "When a jaded actor meets a down-on-her-luck waitress in Paris, their unexpected love story begins — but will it survive the glare of the spotlight?",
      "popularity": 41.1903,
      "poster_path": "/bLqPpqNoKbNbXysIeft1tLIl6KB.jpg",
      "release_date": "2025-09-25",
      "title": "French Lover",
      "video": false,
      "vote_average": 5.794,
      "vote_count": 109
    },
    {
      "adult": false,
      "backdrop_path": "/9BBTo63ANSmhC4e6r62OJFuK2GL.jpg",
      "genre_ids": [
        878,
        28,
        12
      ],
      "id": 24428,
      "original_language": "en",
      "original_title": "The Avengers",
      "overview": "When an unexpected enemy emerges and threatens global safety and security, Nick Fury, director of the international peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull the world back from the brink of disaster. Spanning the globe, a daring recruitment effort begins!",
      "popularity": 41.8055,
      "poster_path": "/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg",
      "release_date": "2012-04-25",
      "title": "The Avengers",
      "video": false,
      "vote_average": 7.821,
      "vote_count": 33384
    },
    {
      "adult": false,
      "backdrop_path": "/86uNq5q9s7LuzVHC8LrdXRMaslk.jpg",
      "genre_ids": [
        27,
        18
      ],
      "id": 1178764,
      "original_language": "cn",
      "original_title": "邪Mall",
      "overview": "When seven malevolent spirits are unleashed to possess the tenants of a dying mall, it falls to a jaded ex-stuntman, his headstrong daughter and a would-be Daoist priest to stop the undead before they spread onto the streets beyond.",
      "popularity": 32.1051,
      "poster_path": "/uNrBz1wrWszAVvOoyWPySbXWoxz.jpg",
      "release_date": "2024-10-31",
      "title": "Possession Street",
      "video": false,
      "vote_average": 8,
      "vote_count": 5
    },
    {
      "adult": false,
      "backdrop_path": "/zWO3B2CTDguZqCrVQ2u5Xnp9VRS.jpg",
      "genre_ids": [
        80,
        53,
        35
      ],
      "id": 1245993,
      "original_language": "en",
      "original_title": "Caught Stealing",
      "overview": "Burned-out ex-baseball player Hank Thompson unexpectedly finds himself embroiled in a dangerous struggle for survival amidst the criminal underbelly of late 1990s New York City, forced to navigate a treacherous underworld he never imagined.",
      "popularity": 38.2364,
      "poster_path": "/yzhay59xLcHwvcsGfpqzh6zcgfG.jpg",
      "release_date": "2025-08-26",
      "title": "Caught Stealing",
      "video": false,
      "vote_average": 6.986,
      "vote_count": 357
    },
    {
      "adult": false,
      "backdrop_path": "/yth78N88nwokepnOe5atwPGfTL1.jpg",
      "genre_ids": [
        28,
        80,
        53
      ],
      "id": 1382406,
      "original_language": "zh",
      "original_title": "惊天大营救",
      "overview": "A veteran Muay Thai expert goes on a take-no-prisoners mission of revenge after his wife and daughter are brutally murdered by mysterious forces.",
      "popularity": 42.4495,
      "poster_path": "/nML8rOI4GOiiEsXgknuhZeUF8M7.jpg",
      "release_date": "2024-12-05",
      "title": "Striking Rescue",
      "video": false,
      "vote_average": 7.5,
      "vote_count": 125
    },
    {
      "adult": false,
      "backdrop_path": "/xSD0q1FiuZkvHuy7uscOLbmd1hR.jpg",
      "genre_ids": [
        12,
        28,
        14
      ],
      "id": 13494,
      "original_language": "en",
      "original_title": "Red Sonja",
      "overview": "A young girl rises from the ashes of tragedy to become the most feared warrior woman of all time: the She-Devil with a Sword.",
      "popularity": 42.2539,
      "poster_path": "/aE3yh4y0h96CZZpLo0UDFMWZAA9.jpg",
      "release_date": "2025-07-31",
      "title": "Red Sonja",
      "video": false,
      "vote_average": 5.781,
      "vote_count": 112
    },
    {
      "adult": false,
      "backdrop_path": "/v23fWgJUEt8EMmvn19btIacxP8E.jpg",
      "genre_ids": [
        10749,
        14,
        16
      ],
      "id": 3933,
      "original_language": "en",
      "original_title": "Corpse Bride",
      "overview": "In a 19th-century European village, a young man about to be married is whisked away to the underworld and wed to a mysterious corpse bride, while his real bride waits bereft in the land of the living.",
      "popularity": 42.2144,
      "poster_path": "/isb2Qow76GpqYmsSyfdMfsYAjts.jpg",
      "release_date": "2005-09-12",
      "title": "Corpse Bride",
      "video": false,
      "vote_average": 7.584,
      "vote_count": 9770
    },
    {
      "adult": false,
      "backdrop_path": "/jvzPTf0nhsqAXodYT6ILKb99IA2.jpg",
      "genre_ids": [
        18,
        10749
      ],
      "id": 1289936,
      "original_language": "en",
      "original_title": "Downton Abbey: The Grand Finale",
      "overview": "When Mary finds herself at the center of a public scandal and the family faces financial strife, the entire household grapples with the threat of social disgrace. The Crawleys must embrace change as the staff prepares for a new chapter with the next generation leading Downton Abbey into the future.",
      "popularity": 39.1507,
      "poster_path": "/m1tLlMTCwc6oK6OjWTOI4b32wnA.jpg",
      "release_date": "2025-09-10",
      "title": "Downton Abbey: The Grand Finale",
      "video": false,
      "vote_average": 7.409,
      "vote_count": 89
    },
    {
      "adult": false,
      "backdrop_path": "/rthMuZfFv4fqEU4JVbgSW9wQ8rs.jpg",
      "genre_ids": [
        28,
        878,
        12
      ],
      "id": 986056,
      "original_language": "en",
      "original_title": "Thunderbolts*",
      "overview": "After finding themselves ensnared in a death trap, seven disillusioned castoffs must embark on a dangerous mission that will force them to confront the darkest corners of their pasts.",
      "popularity": 37.681,
      "poster_path": "/hqcexYHbiTBfDIdDWxrxPtVndBX.jpg",
      "release_date": "2025-04-30",
      "title": "Thunderbolts*",
      "video": false,
      "vote_average": 7.334,
      "vote_count": 2741
    },
    {
      "adult": false,
      "backdrop_path": "/hPUDpQ1W7VZAqBQTi0tZx0EPEsL.jpg",
      "genre_ids": [
        27,
        53
      ],
      "id": 138843,
      "original_language": "en",
      "original_title": "The Conjuring",
      "overview": "Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their farmhouse. Forced to confront a powerful entity, the Warrens find themselves caught in the most terrifying case of their lives.",
      "popularity": 37.1784,
      "poster_path": "/wVYREutTvI2tmxr6ujrHT704wGF.jpg",
      "release_date": "2013-07-18",
      "title": "The Conjuring",
      "video": false,
      "vote_average": 7.541,
      "vote_count": 12246
    },
    {
      "adult": false,
      "backdrop_path": "/cT9ZfwoPDk8JbgkessmQgxAWiaM.jpg",
      "genre_ids": [
        28,
        878,
        53
      ],
      "id": 1071585,
      "original_language": "en",
      "original_title": "M3GAN 2.0",
      "overview": "After the underlying tech for M3GAN is stolen and misused by a powerful defense contractor to create a military-grade weapon known as Amelia, M3GAN's creator Gemma realizes that the only option is to resurrect M3GAN and give her a few upgrades, making her faster, stronger, and more lethal.",
      "popularity": 40.7414,
      "poster_path": "/4a63rQqIDTrYNdcnTXdPsQyxVLo.jpg",
      "release_date": "2025-06-25",
      "title": "M3GAN 2.0",
      "video": false,
      "vote_average": 7.412,
      "vote_count": 889
    },
    {
      "adult": false,
      "backdrop_path": "/k1pJslKr4aDayw9kWwWnlC3MIP3.jpg",
      "genre_ids": [
        16,
        14,
        12
      ],
      "id": 615453,
      "original_language": "zh",
      "original_title": "哪吒之魔童降世",
      "overview": "A young boy is born as the reincarnation of a demonic power, into a society that hates and fears him. Destined by prophecy to bring destruction to the world, Nezha must choose between good and evil to see if he can change his fate.",
      "popularity": 40.4269,
      "poster_path": "/phM9bb6s9c60LA8qwsdk7U1N2cS.jpg",
      "release_date": "2019-07-26",
      "title": "Ne Zha",
      "video": false,
      "vote_average": 7.888,
      "vote_count": 674
    },
    {
      "adult": false,
      "backdrop_path": "/fGrlT6p7IzmujKW0GPBESx2pXCp.jpg",
      "genre_ids": [
        18
      ],
      "id": 1193861,
      "original_language": "es",
      "original_title": "La mujer de la fila",
      "overview": "Andrea visits her incarcerated son for the first time. Initially eyed with suspicion by other women in line, she gradually gains their trust — and emerges as a powerful advocate for justice reform.",
      "popularity": 36.4778,
      "poster_path": "/n1PPU08b3ltuVhL7Dd5A8TMMR7t.jpg",
      "release_date": "2025-09-04",
      "title": "The Woman in the Line",
      "video": false,
      "vote_average": 7,
      "vote_count": 1
    },
    {
      "adult": false,
      "backdrop_path": "/7Q2CmqIVJuDAESPPp76rWIiA0AD.jpg",
      "genre_ids": [
        28,
        12,
        18
      ],
      "id": 1011477,
      "original_language": "en",
      "original_title": "Karate Kid: Legends",
      "overview": "After a family tragedy, kung fu prodigy Li Fong is uprooted from his home in Beijing and forced to move to New York City with his mother. When a new friend needs his help, Li enters a karate competition – but his skills alone aren't enough. Li's kung fu teacher Mr. Han enlists original Karate Kid Daniel LaRusso for help, and Li learns a new way to fight, merging their two styles into one for the ultimate martial arts showdown.",
      "popularity": 38.9,
      "poster_path": "/AEgggzRr1vZCLY86MAp93li43z.jpg",
      "release_date": "2025-05-08",
      "title": "Karate Kid: Legends",
      "video": false,
      "vote_average": 7.115,
      "vote_count": 898
    },
    {
      "adult": false,
      "backdrop_path": "/7QirCB1o80NEFpQGlQRZerZbQEp.jpg",
      "genre_ids": [
        18,
        10749
      ],
      "id": 1156594,
      "original_language": "es",
      "original_title": "Culpa nuestra",
      "overview": "Jenna and Lion's wedding brings about the long-awaited reunion between Noah and Nick after their breakup. Nick's inability to forgive Noah stands as an insurmountable barrier. He, heir to his grandfather's businesses, and she, starting her professional life, resist fueling a flame that's still alive. But now that their paths have crossed again, will love be stronger than resentment?",
      "popularity": 38.6276,
      "poster_path": "/yzqHt4m1SeY9FbPrfZ0C2Hi9x1s.jpg",
      "release_date": "2025-10-15",
      "title": "Our Fault",
      "video": false,
      "vote_average": 0,
      "vote_count": 0
    },
    {
      "adult": false,
      "backdrop_path": "/zav0v7gLWMu6pVwgsIAwt11GJ4C.jpg",
      "genre_ids": [
        27,
        53,
        878
      ],
      "id": 1100988,
      "original_language": "en",
      "original_title": "28 Years Later",
      "overview": "Twenty-eight years since the rage virus escaped a biological weapons laboratory, now, still in a ruthlessly enforced quarantine, some have found ways to exist amidst the infected. One such group lives on a small island connected to the mainland by a single, heavily-defended causeway. When one member departs on a mission into the dark heart of the mainland, he discovers secrets, wonders, and horrors that have mutated not only the infected but other survivors as well.",
      "popularity": 37.8547,
      "poster_path": "/mIg1qCkVxnAlM2TK3RUF0tdEXlE.jpg",
      "release_date": "2025-06-18",
      "title": "28 Years Later",
      "video": false,
      "vote_average": 6.72,
      "vote_count": 1549
    },
{
      "adult": false,
      "backdrop_path": "/mwI1OarF7BVWwn5O1Ng73UhyctP.jpg",
      "genre_ids": [
        27,
        878,
        53
      ],
      "id": 914215,
      "original_language": "en",
      "original_title": "Humane",
      "overview": "In the wake of an environmental collapse that has forced humanity to shed 20% of its population, a family dinner erupts into chaos when a father's plan to enlist in the government's new euthanasia program goes horribly awry.",
      "popularity": 37.2,
      "poster_path": "/ecOvKaoM1hwQbp0uTnULGrSV32e.jpg",
      "release_date": "2024-04-26",
      "title": "Humane",
      "video": false,
      "vote_average": 5.408,
      "vote_count": 179
    },
    {
      "adult": false,
      "backdrop_path": "/kTfSYAxmdUjEI7mUzM2eZ910Puo.jpg",
      "genre_ids": [
        35,
        10749
      ],
      "id": 1096638,
      "original_language": "fr",
      "original_title": "À toute allure",
      "overview": "She is a tactical submarine officer. He is a steward. Before diving for 90 days she chooses to make it her \"four-hour\". But here he is clinging. Who follows her. Who sticks it. Where do we go if the flight crew of a 747 prides itself on romance? As if we had time to be in love on board a military ship. Still, the Pacific Ocean is not big enough. Neither does the Arctic Ocean. The world is so small when you love each other so much.",
      "popularity": 43.8785,
      "poster_path": "/8AgHsphAafmNANTKdBQfKmgGbhi.jpg",
      "release_date": "2024-11-06",
      "title": "In the Sub for Love",
      "video": false,
      "vote_average": 5.315,
      "vote_count": 46
    },
    {
      "adult": false,
      "backdrop_path": "/or8y8JFF0vR3N9ap0Vdhf9tfTxQ.jpg",
      "genre_ids": [
        28,
        80
      ],
      "id": 1028248,
      "original_language": "en",
      "original_title": "As Good as Dead",
      "overview": "An ex-cop in self-imposed witness protection in Mexico becomes a target when a fight video of his apprentice goes viral.",
      "popularity": 39.4702,
      "poster_path": "/qqQPxxRQqfLrq0ubfDQCwhJHZ91.jpg",
      "release_date": "2022-12-16",
      "title": "As Good as Dead",
      "video": false,
      "vote_average": 6.404,
      "vote_count": 73
    },
    {
      "adult": false,
      "backdrop_path": "/zo8CIjJ2nfNOevqNajwMRO6Hwka.jpg",
      "genre_ids": [
        10751,
        35,
        12,
        16,
        14
      ],
      "id": 1241982,
      "original_language": "en",
      "original_title": "Moana 2",
      "overview": "After receiving an unexpected call from her wayfinding ancestors, Moana journeys alongside Maui and a new crew to the far seas of Oceania and into dangerous, long-lost waters for an adventure unlike anything she's ever faced.",
      "popularity": 36.7093,
      "poster_path": "/aLVkiINlIeCkcZIzb7XHzPYgO6L.jpg",
      "release_date": "2024-11-21",
      "title": "Moana 2",
      "video": false,
      "vote_average": 7.036,
      "vote_count": 2737
    },
    {
      "adult": false,
      "backdrop_path": "/nJLp4dBD29fwWopoaranezJ26bo.jpg",
      "genre_ids": [
        53,
        28
      ],
      "id": 1450529,
      "original_language": "es",
      "original_title": "Gatillero",
      "overview": "A former hitman accepts a minor job for his former boss. However, in a corrupt neighborhood, nothing is what it seems, and he quickly gets caught in a web of unexpected events. A race against time and his own destiny.",
      "popularity": 35.4274,
      "poster_path": "/eBw5Vso0K3r8Dzycxz0E0lgC40z.jpg",
      "release_date": "2025-06-12",
      "title": "Gunman",
      "video": false,
      "vote_average": 6.3,
      "vote_count": 10
    },
    {
      "adult": false,
      "backdrop_path": "/2Nti3gYAX513wvhp8IiLL6ZDyOm.jpg",
      "genre_ids": [
        10751,
        14,
        35,
        12
      ],
      "id": 950387,
      "original_language": "en",
      "original_title": "A Minecraft Movie",
      "overview": "Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they'll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.",
      "popularity": 35.9404,
      "poster_path": "/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg",
      "release_date": "2025-03-31",
      "title": "A Minecraft Movie",
      "video": false,
      "vote_average": 6.363,
      "vote_count": 2407
    },
    {
      "adult": false,
      "backdrop_path": "/AllcXFGjhy3NbZSLCnb55LFlNQo.jpg",
      "genre_ids": [
        18,
        35
      ],
      "id": 1079310,
      "original_language": "fr",
      "original_title": "Vingt Dieux",
      "overview": "After the drunk driving death of his father, 18-year-old Totone must look after his younger sister. He pursues the 30k euro Comté cheese award he has no hope of winning in the western part of the French Alps. Good thing Marie-Lise takes a liking for him!",
      "popularity": 30.8776,
      "poster_path": "/k4LIwOO1GvvHcJOHXsynZsQrNYe.jpg",
      "release_date": "2024-12-11",
      "title": "Holy Cow",
      "video": false,
      "vote_average": 7.015,
      "vote_count": 268
    },
    {
      "adult": false,
      "backdrop_path": "/bfh9Z3Ghz4FOJAfLOAhmc3ccnHU.jpg",
      "genre_ids": [
        12,
        14
      ],
      "id": 671,
      "original_language": "en",
      "original_title": "Harry Potter and the Philosopher's Stone",
      "overview": "Harry Potter has lived under the stairs at his aunt and uncle's house his whole life. But on his 11th birthday, he learns he's a powerful wizard—with a place waiting for him at the Hogwarts School of Witchcraft and Wizardry. As he learns to harness his newfound powers with the help of the school's kindly headmaster, Harry uncovers the truth about his parents' deaths—and about the villain who's to blame.",
      "popularity": 35.4415,
      "poster_path": "/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg",
      "release_date": "2001-11-16",
      "title": "Harry Potter and the Philosopher's Stone",
      "video": false,
      "vote_average": 7.9,
      "vote_count": 28683
    },
    {
      "adult": false,
      "backdrop_path": "/uJK0jjJ8QDOQw5lcNBwu059ht4D.jpg",
      "genre_ids": [
        10749,
        18
      ],
      "id": 1294203,
      "original_language": "en",
      "original_title": "My Fault: London",
      "overview": "18-year-old Noah moves from America to London, with her mother who's recently fallen in love with William, a wealthy British businessman. Noah meets William’s son, bad-boy Nick, and soon discovers there is an attraction between them neither can avoid. As Noah spends the summer adjusting to her new life, her devastating past will catch up with her while falling in love for the first time.",
      "popularity": 33.5694,
      "poster_path": "/ttN5D6GKOwKWHmCzDGctAvaNMAi.jpg",
      "release_date": "2025-02-12",
      "title": "My Fault: London",
      "video": false,
      "vote_average": 7.474,
      "vote_count": 473
    },
    {
      "adult": false,
      "backdrop_path": "/by8z9Fe8y7p4jo2YlW2SZDnptyT.jpg",
      "genre_ids": [
        28,
        35,
        878
      ],
      "id": 533535,
      "original_language": "en",
      "original_title": "Deadpool & Wolverine",
      "overview": "A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine.",
      "popularity": 34.21,
      "poster_path": "/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
      "release_date": "2024-07-24",
      "title": "Deadpool & Wolverine",
      "video": false,
      "vote_average": 7.576,
      "vote_count": 7812
    },
    {
      "adult": false,
      "backdrop_path": null,
      "genre_ids": [],
      "id": 1498658,
      "original_language": "it",
      "original_title": "Mantis",
      "overview": "Inside an apartment in Milan, two gay boys talk about their feelings towards dating apps.",
      "popularity": 34.7912,
      "poster_path": "/AvoOuMkzckcd9eozRnoZlpfY12A.jpg",
      "release_date": "2025-09-19",
      "title": "Mantis",
      "video": false,
      "vote_average": 0,
      "vote_count": 0
    },
    {
      "adult": false,
      "backdrop_path": "/uUa6jgSr5BQpcBhhaz1PV1JhSa4.jpg",
      "genre_ids": [
        12,
        28,
        878
      ],
      "id": 20526,
      "original_language": "en",
      "original_title": "TRON: Legacy",
      "overview": "Sam Flynn, the tech-savvy and daring son of Kevin Flynn, investigates his father's disappearance and is pulled into The Grid. With the help of a mysterious program named Quorra, Sam quests to stop evil dictator Clu from crossing into the real world.",
      "popularity": 38.5571,
      "poster_path": "/vuifSABRpSnxCAOxEnWpNbZSXpp.jpg",
      "release_date": "2010-12-14",
      "title": "TRON: Legacy",
      "video": false,
      "vote_average": 6.5,
      "vote_count": 7499
    },
    {
      "adult": false,
      "backdrop_path": "/6nCy4OrV7gxhDc3lBSUxkNALPej.jpg",
      "genre_ids": [
        28,
        36,
        18,
        12,
        10752
      ],
      "id": 1051486,
      "original_language": "en",
      "original_title": "Stockholm Bloodbath",
      "overview": "In 1520, the notorious and power-hungry Danish King Christian II is determined to seize the Swedish crown from Sten Sture, no matter what it takes. Meanwhile, sisters Freja and Anne make a solemn promise to seek revenge on the men who brutally murdered their family. Everything comes to a head in the heart of Stockholm, where the sisters are drawn into a ruthless political struggle between Sweden and Denmark that culminates in a mass execution, presided over by the mad King \"Christian the Tyrant,\" known as the Stockholm Bloodbath.",
      "popularity": 36.2603,
      "poster_path": "/tzXOB8nxO70SfSbOhrYcY94x6MI.jpg",
      "release_date": "2024-01-19",
      "title": "Stockholm Bloodbath",
      "video": false,
      "vote_average": 6.3,
      "vote_count": 35
    },
    {
      "adult": false,
      "backdrop_path": "/nAxGnGHOsfzufThz20zgmRwKur3.jpg",
      "genre_ids": [
        27,
        28,
        53
      ],
      "id": 1233413,
      "original_language": "en",
      "original_title": "Sinners",
      "overview": "Trying to leave their troubled lives behind, twin brothers return to their hometown to start again, only to discover that an even greater evil is waiting to welcome them back.",
      "popularity": 32.4456,
      "poster_path": "/4CkZl1LK6a5rXBqJB2ZP77h3N5i.jpg",
      "release_date": "2025-04-16",
      "title": "Sinners",
      "video": false,
      "vote_average": 7.537,
      "vote_count": 2683
    },
    {
      "adult": false,
      "backdrop_path": "/7FTOVQO3pndOuswVS3iNt3odVq5.jpg",
      "genre_ids": [
        27
      ],
      "id": 691363,
      "original_language": "fr",
      "original_title": "La Chose derrière la porte",
      "overview": "The story of Adèle, a youg woman who’s literally haunted by the death of her husband, killed in a trench during World War One. Desperate and unable to face this tragic loss, Adèle turns to black magic in the hope to bring her lover back. The miracle will happen, but Adèle will have to pay the price for opening this Pandora Box.",
      "popularity": 31.9557,
      "poster_path": "/ouEJLj6eZuQZUZ6bf69RJg9g0lQ.jpg",
      "release_date": "2023-01-16",
      "title": "The Thing Behind The Door",
      "video": false,
      "vote_average": 5.652,
      "vote_count": 23
    },
    {
      "adult": false,
      "backdrop_path": "/bVSOgrxasVJF6V71T7v2KfBRSzu.jpg",
      "genre_ids": [
        18,
        27,
        878
      ],
      "id": 933260,
      "original_language": "en",
      "original_title": "The Substance",
      "overview": "A fading celebrity decides to use a black market drug, a cell-replicating substance that temporarily creates a younger, better version of herself.",
      "popularity": 34.5584,
      "poster_path": "/lqoMzCcZYEFK729d6qzt349fB4o.jpg",
      "release_date": "2024-09-07",
      "title": "The Substance",
      "video": false,
      "vote_average": 7.1,
      "vote_count": 5163
    },
    {
      "adult": false,
      "backdrop_path": "/nt0HRxlzOXRpPJtl2FmeBCO6MeR.jpg",
      "genre_ids": [
        27,
        14,
        10749
      ],
      "id": 1246049,
      "original_language": "fr",
      "original_title": "Dracula",
      "overview": "The story of 15th century Prince Vladimir who curses God following the death of his beloved wife and is turned into a vampire. Later, in 19th century London, he discovers his wife’s doppelgänger and dooms himself by pursuing her.",
      "popularity": 33.8399,
      "poster_path": "/ykyRfv7JDofLxXLAwtLXaSuaFfM.jpg",
      "release_date": "2025-07-30",
      "title": "Dracula: A Love Tale",
      "video": false,
      "vote_average": 7.217,
      "vote_count": 182
    },
    {
      "adult": false,
      "backdrop_path": "/djkxDBzBEL3xShrtpJP17L8V2U3.jpg",
      "genre_ids": [
        27,
        9648,
        53
      ],
      "id": 1083433,
      "original_language": "en",
      "original_title": "I Know What You Did Last Summer",
      "overview": "When five friends inadvertently cause a deadly car accident, they cover up their involvement and make a pact to keep it a secret rather than face the consequences. A year later, their past comes back to haunt them and they're forced to confront a horrifying truth: someone knows what they did last summer…and is hell-bent on revenge.",
      "popularity": 33.4649,
      "poster_path": "/6BfZLQYj97NO1yD5JkSAf5vWzGj.jpg",
      "release_date": "2025-07-16",
      "title": "I Know What You Did Last Summer",
      "video": false,
      "vote_average": 5.934,
      "vote_count": 439
    },
    {
      "adult": false,
      "backdrop_path": "/yGsEsHlc81Q1i9Z3yoVv394PrlG.jpg",
      "genre_ids": [
        10749,
        18
      ],
      "id": 1319965,
      "original_language": "en",
      "original_title": "All of You",
      "overview": "Laura and Simon have been best friends since college. Over the years, they've realized their bond is more than platonic. Can they—and should they—risk everything to explore a love that has existed all along?",
      "popularity": 28.4327,
      "poster_path": "/U2KFjtXN9NL9eCENB28w42Jakp.jpg",
      "release_date": "2025-09-26",
      "title": "All of You",
      "video": false,
      "vote_average": 6.169,
      "vote_count": 68
    },
    {
      "adult": false,
      "backdrop_path": "/28zX1DO1NJWeS1e573lSJQ9kiVh.jpg",
      "genre_ids": [
        27,
        53
      ],
      "id": 604079,
      "original_language": "en",
      "original_title": "The Long Walk",
      "overview": "In a dystopian, alternate-America ruled by a totalitarian regime, 50 teenage boys take part in a deadly annual walking contest, forced to maintain a minimum pace or be executed, until only one survivor remains.",
      "popularity": 33.554,
      "poster_path": "/wobVTa99eW0ht6c1rNNzLkazPtR.jpg",
      "release_date": "2025-09-10",
      "title": "The Long Walk",
      "video": false,
      "vote_average": 7.059,
      "vote_count": 247
    }

]""".trimIndent()

    private val movies: List<Movie> = json.decodeFromString(movieRawData)

    // 부록 1 - 영화 데이터

    override suspend fun searchByTitle(query: String): List<Movie> {
        val allMovies: List<Movie> = json.decodeFromString(movieRawData)
        return allMovies.filter { movie ->
            movie.title.contains(query, ignoreCase = true) ||
                    movie.original_title.contains(query, ignoreCase = true)
        }
    }
    // [추가] 2. 인터페이스에 정의된 함수를 실제로 구현합니다.
    // 메모리에 이미 로드된 movies 리스트에서 id가 일치하는 첫 번째 영화를 찾아 반환합니다.
    override fun getMovieById(id: Long): Movie? {
        return movies.find { it.id == id }
    }
}

