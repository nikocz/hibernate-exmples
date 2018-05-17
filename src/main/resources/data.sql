insert into galaxy (id, name) values
(1, 'Milky Way');

insert into star (id, name, galaxy_id) values
(1, 'Sun', 1),
(2, 'Proxima Centauri', 1),
(3, 'VY Canis Majoris', 1);

insert into planet (id, name, position, star_id) values
(1, 'Mercury', 1, 1),
(2, 'Venus', 2, 1),
(3, 'Earth', 3, 1),
(4, 'Mars', 4, 1),
(5, 'Jupiter', 5, 1),
(6, 'Saturn', 6, 1),
(7, 'Uranus', 7, 1),
(8, 'Neptune', 8, 1),
(9, 'Proxima b', 1, 2);
