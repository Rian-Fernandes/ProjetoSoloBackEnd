package com.teste.primeiroexemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiroexemplo.repository.ProdutoRepository;
import com.teste.primeiroexemplo.shared.ProdutoDTO;

@Service
public class ProdutoService {
    
    @Autowired 
    private ProdutoRepository produtoRepository;

    public List<ProdutoDTO> obterTodos(){

       //retorna uma lista de produto model 
       List <Produto> produtos = produtoRepository.findAll();
       return produtos.stream().map(produto -> new ModelMapper().map(produto, ProdutoDTO.class)).collect(Collectors.toList());
    }

    public Optional<ProdutoDTO> obterPorId(Integer id){
        //Obtendo optional de produto por id
        Optional<Produto> produto = produtoRepository.findById(id);

        //se nao encontrar, lança exception
        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado");
        }

        //convertendo optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
        
        //Criando e retornando um optional de produtoDto.
        return Optional.of(dto);
    }

    public ProdutoDTO adicionar(ProdutoDTO produtoDto){
        //removendo o id para conseguir fazer o cadastro
        produtoDto.setId(null);

        //Criando um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        //Convertendo o produtoDto em um produto
        Produto produto = mapper.map(produtoDto, Produto.class);

        //Salvar o produto no banco
        produto = produtoRepository.save(produto);
        produtoDto.setId(produto.getId());

        //Retornar o ProdutoDTO atualizado
        
        return produtoDto;
    }

    public void deletar(Integer id){
        //verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);

        //se nao existir, lança uma exception
        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Não foi possível deletar o produto com o id " + id);
        }

        //deleta o produto pelo id
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto){
        //Passar o id para o produtoDto
        produtoDto.setId(id);

        //Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        
        //Converter o dto em um produto 
        Produto produto = mapper.map(produtoDto, Produto.class);

        //Atualizar o produto no banco de dados
        produtoRepository.save(produto);

        //Retornar o prodcutoDto atualizado
        return produtoDto;

    }
}
