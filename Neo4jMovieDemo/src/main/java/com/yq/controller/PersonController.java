package com.yq.controller;import com.yq.domain.Movie;import com.yq.domain.Person;import com.yq.repository.MovieRepository;import com.yq.repository.PersonRepository;import com.yq.service.PersonService;import org.slf4j.LoggerFactory;import org.slf4j.Logger;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.domain.Page;import org.springframework.data.domain.PageRequest;import org.springframework.data.domain.Sort;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.bind.annotation.RestController;import java.util.Collection;/** */@RestController@RequestMapping("/person")public class PersonController {    private static Logger log =  LoggerFactory.getLogger(PersonController.class);    private final PersonService personService;	public PersonController(PersonService personService) {		this.personService = personService;	}    @Autowired    PersonRepository personRepository;    @Autowired    MovieRepository movieRepository;    @GetMapping("/findByfirstName")    public Person findByfirstName(@RequestParam String name) {        return personService.findByfirstName(name);    }    @GetMapping("/findByNameFilter")    public  Collection<Person> findByNameFilter(@RequestParam String name) {        return personRepository.findByNameFilter(name);    }    @GetMapping("/likeName")    public Collection<Person> findByNameLike(@RequestParam String name) {        return personService.findByNameLike(name);    }    @GetMapping("/findById")    public Iterable<Person> findByNodeId(@RequestParam Integer id) {        return personRepository.findByNodeId(Person.class, id);    }    @GetMapping("/findActorByMovieTitle")    public Page<Person> findActorByMovieTitle(@RequestParam(value = "page", defaultValue = "0") Integer page,                                    @RequestParam(value = "size", defaultValue = "15") Integer size,                                    @RequestParam String title) {        Sort sort = new Sort(Sort.Direction.DESC, "p.firstName");        PageRequest pageRequest = PageRequest.of(page, size, sort);        return personRepository.getActorsThatActInAmoviesFromTitle(title, pageRequest);    }    @GetMapping("/all")    public Iterable<Person> findAll() {        return personRepository.findAll();    }    @GetMapping("/clean")    public Iterable<Person> clean() {        //clean work        Iterable<Person> persons = personRepository.findAll();        for (Person person : persons) {            person.setAMovies(null);            personRepository.save(person);        }        personRepository.deleteAll();        movieRepository.deleteAll();        return personRepository.findAll();    }    @GetMapping("/init")    public Iterable<Person> init() {        //clean work        Iterable<Person> persons = personRepository.findAll();        for (Person person : persons) {            person.setAMovies(null);            personRepository.save(person);        }        personRepository.deleteAll();        movieRepository.deleteAll();        //以下数据纯属虚构        //参演了 《建国大业A》        Person clPerson  = new Person("ChengLongP", "Jack", 175);        //参演了《长城B》        Person jtPerson = new Person("JingTianP", "JT", 170);        //参演了《长城B》 《建国大业A》        Person ldhPerson = new Person("LiuDeHuaP", "DeHua", 180);        //导演了《长城B》        Person zymPerson  = new Person("ZhangYiMouP", "YiMou", 176);        //导演了《建国大业A》        Person hspPerson  = new Person("HamSamPingP", "HamDao", 186);//        personRepository.save(clPerson);//        personRepository.save(jtPerson);//        personRepository.save(ldhPerson);//        personRepository.save(zymPerson);        Movie jgMovie = new Movie("建国大业A" , "history", 9000);       // movieRepository.save(jgMovie);        Movie clMovie = new Movie("长城B", "science fiction", 5000);        //movieRepository.save(clMovie);        clPerson.addActMovie(jgMovie);        jtPerson.addActMovie(clMovie);        ldhPerson.addActMovie(jgMovie);        ldhPerson.addActMovie(clMovie);        zymPerson.addDirectMovie(clMovie);        hspPerson.addDirectMovie(jgMovie);        personRepository.save(clPerson);        personRepository.save(jtPerson);        personRepository.save(ldhPerson);        personRepository.save(zymPerson);        personRepository.save(hspPerson);        return personRepository.findAll();    }}