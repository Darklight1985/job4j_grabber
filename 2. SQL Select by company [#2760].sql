select p.name as сотрудник, c.name as Компания from person as p
join company c on
p.company_id = c.id
where c.id <> 5;


select c.name, count(p.id) from person as p
join company c on p.company_id = c.id
group by c.name 
order by count(p.id) desc
limit 1