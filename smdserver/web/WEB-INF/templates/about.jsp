<h2>Идея и&nbsp;цели</h2>
<p>Основная цель&nbsp;&mdash; создать удобный инструмент, помогающий в&nbsp;изучении иностранных слов.
</p><p>Когда начинаешь изучать иностранный язык, то&nbsp;количество знакомых слов буквально за&nbsp;пару недель достигает нескольких сотен. Все эти слова нужно регулярно повторять, чтобы запомнить. Какие-то помнишь уже хорошо, какие-то ещё нет, постоянно появляются новые слова. Вот они уже записаны на&nbsp;нескольких листах, и&nbsp;становится сложно выискивать те&nbsp;слова, которые ты&nbsp;ещё плохо знаешь. Т.е. некоторые слова постоянно мозолят глаза, хотя ты&nbsp;их&nbsp;уже хорошо запомнил, а&nbsp;некоторые умудряются всё время ускользать из&nbsp;внимания.
</p><p>Появилась идея, сделать инструмент, который вёл&nbsp;бы рейтинг твоих слов и&nbsp;чаще напоминал пользователю слова, которые тот плохо знает. А&nbsp;те&nbsp;слова, которые пользователь запомнил хорошо, инструмент показывал&nbsp;бы реже.

</p><p>Причём, инструмент должен быть таким, чтобы можно было легко повторить слова в&nbsp;любую свободную минуту: в&nbsp;маршрутке или в&nbsp;очереди. Для этого хорошо подходит приложение к&nbsp;мобильному телефону.
</p><p>А&nbsp;ещё этот инструмент должен быть удобен для добавления новых слов. Чаще всего незнакомые слова встречаются при чтении текстов в&nbsp;интернете. Значит надо уметь добавлять слова из&nbsp;браузера. В&nbsp;идеале, это должен быть удобный плагин к&nbsp;браузеру. Даже если вы&nbsp;получили список новых слов на&nbsp;очередном занятии по&nbsp;иностранному языку. Вводить большое количество слов через телефон не&nbsp;удобно. Гораздо удобнее сделать это на&nbsp;компьютере.
</p><p>Вот и&nbsp;решили начать с&nbsp;программки для телефона, который синхронизировался&nbsp;бы с&nbsp;сайтом. На&nbsp;телефоне повторяем слова, на&nbsp;сайте можем надобавлять новых.
</p>

<h2>JavaME приложение</h2>
<p>Начали с&nbsp;JavaME, потому что на&nbsp;тот момент у&nbsp;нас были такие телефоны. И&nbsp;сейчас есть.</p>

<p>Программа для мобильного телефона создана Сергеем Скрипниковым, учеником 88&nbsp;школы.
Его никто не&nbsp;обучал платформе JavaME, а&nbsp;он&nbsp;сам во&nbsp;всём разобрался.
</p><p>Вот так оно выглядит:
<a href="http://img-fotki.yandex.ru/get/5810/10178070.19a/0_7a521_34767816_orig" class="external text" rel="nofollow"><img src="http://img-fotki.yandex.ru/get/5810/10178070.19a/0_7a521_34767816_M.jpg" alt="0_7a521_34767816_M.jpg" style="vertical-align:middle"/></a><br>
Вот так тестируется знание слов:
<a href="http://img-fotki.yandex.ru/get/4409/10178070.19a/0_7a52b_96b5299e_orig" class="external text" rel="nofollow"><img src="http://img-fotki.yandex.ru/get/4409/10178070.19a/0_7a52b_96b5299e_M.jpg" alt="0_7a52b_96b5299e_M.jpg" style="vertical-align:middle"/></a><br>
А&nbsp;так мы&nbsp;узнаём результат теста: 
<a href="http://img-fotki.yandex.ru/get/4519/10178070.19a/0_7a52a_e52028b_orig" class="external text" rel="nofollow"><img src="http://img-fotki.yandex.ru/get/4519/10178070.19a/0_7a52a_e52028b_M.jpg" alt="0_7a52a_e52028b_M.jpg" style="vertical-align:middle"/></a><br>

Рейтинг правильно введённых слов увеличивается и&nbsp;в&nbsp;следующий раз они появляются с&nbsp;меньшей вероятностью.
</p><p>Сразу отвечу на&nbsp;частозадаваемый вопрос:<br />
</p>
<blockquote><i>Зачем мы&nbsp;просим пользователя вводить слово, когда можно просто спрашивать: помнит&nbsp;/ не&nbsp;помнит? Не&nbsp;будет&nbsp;же он&nbsp;сам себя обманывать, зато экономится куча времени.</i></blockquote>
<p>Мы&nbsp;совершенно согласны с&nbsp;этим замечанием и&nbsp;обязательно сделаем такой режим теста. 
</p><p>Но&nbsp;также скажем, что не&nbsp;стоит воспринимать текущий режим, как лишний. Дело в&nbsp;том, что когда человек записывает слово, он&nbsp;лучше его запоминает. Включается механическая память. Кроме того, если никогда не&nbsp;записывать слова, то&nbsp;проявляется такой эффект: слово знакомое, человек узнаёт его в&nbsp;тексте, знает как произносится, но&nbsp;<b>не&nbsp;может его записать,</b> потому что он&nbsp;ни&nbsp;разу этого не&nbsp;делал. Чаще мы&nbsp;можем наблюдать тот&nbsp;же эффект с&nbsp;речью. Человек слушает и&nbsp;смотрит фильмы&nbsp;&mdash; прекрасно воспринимает речь на&nbsp;слух, много читает и&nbsp;пишет, знает слова, знает как они произносятся. Он&nbsp;прекрасно формулирует мысли, но&nbsp;лишь в&nbsp;уме и&nbsp;письменно. Если он&nbsp;не&nbsp;разговаривает на&nbsp;иностранном языке, то&nbsp;с&nbsp;устной речью у&nbsp;него будут проблемы.
</p><p>Сейчас нельзя сказать, что это приложение закончено и&nbsp;абсолютно удобно. К&nbsp;приложению есть ряд пожеланий и&nbsp;оно ещё будет дорабатываться. Но&nbsp;пользоваться им&nbsp;можно уже сейчас. Я&nbsp;пользуюсь.
</p>

<h2>Сайт</h2>
<p>Как было сказано выше, сайт нужен для того, чтобы не&nbsp;тратить сто лет на&nbsp;добавление ста слов.
</p><p>Сейчас через него можно добавлять и&nbsp;удалять слова.
</p>

<h2>Известные ошибки</h2>
<p>Точнее, основные из&nbsp;известных:
</p>
<ul class="ul">
	<li>Синхронизация телефона с&nbsp;сайтом не&nbsp;работает, если слов больше 15.
</li><li>Если слов больше&nbsp;50, то&nbsp;процесс тестирования начинает заметно тормозить: долго обрабатывает результат и&nbsp;долго подбирает новую партию слов.
</li></ul>

<p>Над этими ошибками мы&nbsp;собираемся работать в&nbsp;первую очередь. Вот только начнётся учебный год...
</p>

<h2>Установка</h2>
<p>Приложение можно установить на&nbsp;мобильный телефон с&nbsp;поддержкой JavaME.
</p><p>Для этого надо загрузить на&nbsp;телефон и&nbsp;установить следующий файл:
<a href="http://narod.ru/disk/23418375001/SmartDictionary.jar.html" class="external free" rel="nofollow">http://narod.ru/disk/23418375001/SmartDictionary.jar.html</a>
</p>