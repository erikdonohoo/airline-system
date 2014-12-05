<a name="0.13.0"></a>
## 0.13.0 (2014-10-06)


#### Bug Fixes

* **applicant:** fix duplicates with multiple applications and favorites ([d9c98051](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/d9c98051a146575109df2b7eabf1328554d4d322))


#### Features

* **Application:** inprogress application endpoint ([5e2f6bf5](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/5e2f6bf51e184b8b800f55c941ef8bb3a1e1ce0b))
* **Comment:** allow hr to edit all comments ([ac1b83c3](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ac1b83c3ab1763c89b4dadc23080b6d4fd30c2ff))
* **field-add-constraints:** add constraints for fields in backend ([750d80ca](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/750d80caa6000ba6ca9cd6933f46a41caf43613f))


<a name="0.12.0"></a>
## 0.12.0 (2014-09-26)


#### Bug Fixes

* handle new person schema everywhere needed ([799071a1](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/799071a1ce14dcbcb50c0087d5aeb9ac703b6e51))
* **Permissions:** allow hr to see all applications ([c1b10473](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/c1b104734413f665cd492e058a3d1ae6eab62f39))


<a name="0.11.0"></a>
## 0.11.0 (2014-09-11)


#### Bug Fixes

* **Application:** add missing availabilityDate field ([54434189](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/5443418935bfa0626466fe1c038c744803dd3745))
* **Filter:**
  * add fieldids when getting a filter ([210ce38e](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/210ce38e5b3fffc94b40b032af3f3e3c83f4c746))
  * save listorder of created filter ([dbd5548a](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/dbd5548ab1fcb3c220128c23dabb45dc491f728a))
  * generalize postman, fix null error in Filter Resource, and remove rollback from  ([0462a372](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/0462a372979575e9ab93b55d9e71c948e2a30bed))
  * allow no default ([87be34a3](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/87be34a37ac51ba8c6612f0298f65d673ee46d6c))
* **Position:** add missing limited Boolean ([bb29f0df](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/bb29f0df2e7e7b2c7e0db328e97dfd353cd921c0))
* **Rating:** fix posting of ratings ([af852d75](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/af852d75bded5ffb83239b2e3094da512dca3a5a))
* **Recirculate:** fix null pointer exception when there is no interview score ([759678bf](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/759678bfb25a0c9b7d273121e5d2184549023e76))
* **Thread:** send empty comment arrays ([df81c37c](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/df81c37c9f2f7ec4023b36f3734a632e5f4297bf))
* **filter-resource:** change code to account for null default filter while adding new filter ([e77cff8d](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/e77cff8d3d85084464f7d3ea6b36bdcde1c756cc))
* **hire-email:** add changes that were supposed to go into the last commit ([e53b11ac](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/e53b11ac52dbcb534f1921b99b4da809e09c196a))
* **permissions:** fix unintentionally recursive call ([863850af](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/863850afa925b619aae3b9aba86866fca78df8c6))


#### Features

* **Application:**
  * add extra fields to new applications, and cut down the jungle of dao query code ([7ce46881](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/7ce46881da1010c4833c0d1359f8a98070de48d8))
  * return all data that the query does in an application get ([de382a78](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/de382a788c5b1c570057cc04032a6cae6bb05d7f))
  * add visitors center and senior missionary info ([f609ea75](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/f609ea75bc4ae10195e16ade9cf8001e8c3ffbe1))
* **DefaultFilter:** set filter as default ([0d675344](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/0d675344035e7d8478a644e8c77282ef2b34cdd5))
* **Filter:**
  * add get Default endpoint ([4414a50f](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/4414a50f0e83489db97b3084f0ad83cfe87a541b))
  * update filter postman api to include update and default ([950d8374](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/950d83746efdb624fc952a4693c5c61da59b658f))
* **Permissions:** consider jobs in employee manager for permissions ([6830d88c](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/6830d88cc3a0ce1cae6cc797e66f702d6303d529))
* **hire-email:** add functionality to send an email to appropriate persons when an individual is  ([72a70214](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/72a70214e7fc4c177c3198c037015d0861565960))
* **hire-endpoint:** add functionality to send an email to hr with the relevant information ([16a90d63](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/16a90d634e93f21ac783d8f5dce38bb28720e60d))
* **limited-permission:** correct the logic that uses limited permission ([fae153fe](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/fae153fe80a14ca8748c2a8bcf4d557059db5352))
* **limited-permissions:** add limited permissions and detect them on appropriate calls ([86bd4fa3](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/86bd4fa3aac587c1b686c10ca57fa5053ccc89b4))


<a name="0.10.0"></a>
## 0.10.0 (2014-08-27)


#### Bug Fixes

* fix merge issue ([051239e3](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/051239e3c188c834fb0fa60268f6a386b46de77e))
* **Applicant:**
  * fix get on old applicant ([328071dc](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/328071dc4152468ff79e7c99fac61757981b43eb))
  * use correct identifier for byuid and remove duplicates ([f1ab73be](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/f1ab73bea105b71e28e6737a69a5e8f62c7366e5))
* **ApplicantInfo:** show contact info from old applications ([71f6a74b](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/71f6a74bdd5a6c033e8c7fa0fac35e4f733f4f1c))
* **Application:**
  * add null check to assign language endpoint ([c1106131](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/c1106131f98bcad40ad7aab5fd25d182d63ab6a9))
  * check for nulls in assignLanguages on Application ([67c81fd2](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/67c81fd279306a7d546f91bb13499ee987e5aaf2))
  * add DISTINCT keyword to query old applicants and use correct identifier for byuI ([72989a69](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/72989a69efdf1fbf28e7f4d0bfd7361d3b2c7424))
  * define oldToNewPos map correctly ([7ef1268d](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/7ef1268d44639cf9a12e6f12e9fb187d1509c9a5))
* **Comment:** fix query to return an array of comments rather than a thread ([56309d48](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/56309d48b4b0ac3b66239f9faa3326da6c3f09e1))
* **Favorite:** fix favorite, applicant name, and various other bugs ([7dd2ede8](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/7dd2ede8fd89a05d5eb959a47ce000fc631c3b5f))
* **Filter:** query by positionId, add positionId and defaultFilter to Filter, add property to ([d55354b7](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/d55354b76738225862ea2c657970584439554162))
* **Mission:** fix regression that caused missions to 404 ([ddfed550](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ddfed550875a0ac82213e50294d57ce90a25c94d))
* **MissionInfo:** get info with lds/byu ([9d572632](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/9d572632203f46d40689a3b6a628e2248339ba88))
* **Rating:** provide list order for old rating, use ResultSet builder ([74e295ec](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/74e295ec4a2146e270a148214b60c68fb900ad48))
* **retrofit:**
  * add byuid to table ([ea0e5501](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ea0e5501ad1d03b6b6ca3654916b9954df465edb))
  * old applications should get a new threadid ([f2eb9b1c](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/f2eb9b1c5e863d0342c6e927d9af5531f1b9110c))


#### Features

* **Application:**
  * add avgrating and languages to old applications ([8cf877a2](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/8cf877a22d6b3a0ec744aaa3d5d12117fcc3051e))
  * add visitor center and senior missionaries to application ([3d30c025](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/3d30c02581de272d6b4f62ff1bcc7a528ecf9ce6))
  * and languages field to Application based on values ([2544b705](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/2544b705a4e617ba8821bb31c5948381621894fe))
* **Rating:** add ratings from old applications ([88694d07](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/88694d07e4b8b543a3c12b24de2d83fb9e8f9be5))
* **enum:** add interviewer ([2a924338](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/2a924338f88c67c175a1209a0cee5e85ba609725))


<a name="0.9.0"></a>
## 0.9.0 (2014-08-20)


#### Bug Fixes

* **Application:**
  * remove duplicates from favorited applications ([0e63be56](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/0e63be566946e7632d9914e378d7ec7dd6cb759b))
  * check for null interviewScores on recirculate and deny, and set interviewedBy to ([7311da70](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/7311da70c79f952ea729f84bfcdbb3b4e1092eb5))
  * turn off autocommit for createApplication ([18977c92](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/18977c92d19ed0408c545e2be956705cf554bceb))
  * coalesce old position ids to new ids correctly ([ac2c172b](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ac2c172bddba323a03b895fe13e641d07db6877c))
  * invert overwriting values, prefer new over old ([38dea27f](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/38dea27f302e68ad667b7f3b7e09177560209938))
  * change update to update threadId on an application ([c3310db5](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/c3310db562b6ad16ea38892ffa6e598f79f8e13d))
* **ApplicationDao:**
  * don't exclude applicants who don't have a mtc_miss_id ([97505d7d](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/97505d7d4131431eaf46f8bc9c865a5066887ef4))
  * move static code out of constructor ([ca56bff1](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ca56bff116dd17ebe00741af4e6194734abb7391))
* **ApplicationPermission:**
  * fix sql to include additional application permissions ([14c09180](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/14c09180ed5fdcc2e532e453931cc53dd4e7af4a))
  * check mtcid when determining position relations ([677271e3](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/677271e3f956faed0e52eccc1f2b4f1eaf940b90))
* **Filter:** prevent getFilter throwing NullPointerException ([deadf3ea](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/deadf3eab29a384a6101c3fed4016fbe734d0fdf))
* **Header:** fix location header to include Access-Control-Expose-Headers ([c4649be0](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/c4649be0c9f936b2054de5d6f55c5eebb62de51c))
* **JobDescription:** add UNKNOWN value to job description approval status enum ([36fcd45b](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/36fcd45b39b3e9db8aecea3dd97e2e801dc9d93a))
* **deploy:** fix deploy for test/beta servers ([11be3780](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/11be37809622bb7d54c5763a90768a0055531da7))
* **grunt:** fix reduce function, better naming ([47160169](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/471601694ceb77ca240e4182011af38107c6bf73))


#### Features

* **ApplicantManager:** Add the post ApplicationRating endpoint ([2b45a828](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/2b45a828fff6e77fb0a3bbe83c86635b81e6d888))
* **Application:**
  * changed status to enum ([153500fb](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/153500fb2b545abc22db73cfe4d93c84f6e5c459))
  * handle interview changes in recirculate and deny ([52909c08](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/52909c08a5e1ef3fefb4ff2f2504c641e29f3e2a))
  * initialize thread on application creation ([ef60a1d4](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ef60a1d49a4a18dd7ce0e85740b6b3e516991872))
  * create initial applicationfieldvalues when creating an application ([acc84a8d](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/acc84a8df1eaa8ab8c6270e210d6aa06e0cfd03d))
  * create interview endpoint and move interview changes from update to recirculate ([33e29a02](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/33e29a02a02ec8264592d044a15aaa57b63d0e3e))
  * calculate percent complete for application on backend ([ba81bfdc](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ba81bfdc1ee040bd821f8b6fed31e5ecff071e8a))
  * return values list on applications query ([557132bd](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/557132bd2259cefc0d7a0aff4d99d8b7596d4ff1))
  * add thread to application ([174d9fc5](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/174d9fc5ace5606451738fce49a1238219ae615c))
  * add permission checks to application resources ([4bb603d7](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/4bb603d76d57512c9511ffa99bcd9142dda35bff))
* **ApplicationPermission:**
  * add position relations and permissions ([46c6ac33](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/46c6ac3313ae76880bcc0b740f1d3b99a61e897f))
  * implement the stubbed resource and dao ([47e8068a](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/47e8068ac923cd4f4243968acbf6bb78e0ca9203))
* **ApplicationPermissions:**
  * use position name if mtcposition name is null ([b95e7fda](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/b95e7fda79d20888984d16193687ed2ef8034017))
  * add query endpoint to get user's position permissions ([6a1eff01](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/6a1eff0137f1785a5d19bf858bc4ff35d9dc634d))
* **Field:** convert type into an enum ([656bc254](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/656bc25478be3377cf0e2d41d8735c7beb8477ba))
* **JobDescription:** convert jobdescription approval status to an enum ([ce8e8a61](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/ce8e8a61ae5a6d7d9fadd9cf5afac02604c2f956))
* **MissionInfo:** add endpoints to fetch mission information ([6eca3f19](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/6eca3f19eec470191356ee27705ce5bf67cc2bb7))
* **comment:**
  * add proper http error and success codes ([7e6580ee](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/7e6580eed45a1d0fe9da0315187f496270ec800a))
  * Hide comments/threads according to white/blacklists ([91d61108](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/91d6110820d08b155e0e5381d74138d04c1b7007))
  * add whitelists and blacklists ([69fbcb30](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/69fbcb302bf93344f384b331532a0f14ea7e58e6))
* **environments:** add beta/test ([1d9791b8](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/1d9791b8ed2a11eadd4e5afc42df13b1ae13d8e7))
* **filters:** add dao, model, and resource for filter and model for filterfieldvalue ([97da4d80](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/97da4d80aced467d3cd772b84df2c5060ac9cf2b))
* **grunt:** add initial work for grunt ([2fc36ed7](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/2fc36ed733cbaaeca5bef344f2c96cd24235ffb9))
* **rating:**
  * Endpoint to allow updating of ratings ([dc99b656](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/dc99b656dd8180ff6a74702bac57ce04126b821b))
  * Finish all Rating Question and RQ X JD endpoints ([014cdf7e](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/014cdf7e3a514d55090ff3f5d67d3bb3458999c8))
  * add getting rating questions by job description id ([344f397c](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/344f397c7aba656d809778f11d45f2c3b81882c5))
* **workexperiencedao:** add permissions check to get method of workexperienceresource. ([2d6bb486](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/2d6bb486644500f6bb5314b80fbef464e969b63b))
* **workexperienceresource:** add permissions checks to rest of endpoints in workexperienceresource ([09aa9d44](http://gitlab.mtc.byu.edu/java-api/applicant-manager/commit/09aa9d447fc2780d0f7c9fa2ebae13224c584299))

